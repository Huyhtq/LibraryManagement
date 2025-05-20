document.addEventListener('DOMContentLoaded', () => {
    console.log("DOM loaded");

    // Toggle sidebar
    const sidebarToggle = document.getElementById('sidebarToggle');
    const sidebarClose = document.getElementById('sidebarclose');
    const sidebar = document.getElementById('sidebar');
    const content = document.getElementById('content');

    if (sidebarToggle) {
        console.log("Sidebar toggle found");
        sidebarToggle.addEventListener('click', function () {
            console.log("Toggle clicked");
            sidebar.classList.toggle('hidden');
            content.classList.toggle('expanded');
        });
    } else {
        console.log("Sidebar toggle NOT found");
    }

    if (sidebarClose) {
        console.log("Sidebar close found");
        sidebarClose.addEventListener('click', function () {
            console.log("Close clicked");
            sidebar.classList.toggle('hidden');
            content.classList.toggle('expanded');
        });
    } else {
        console.log("Sidebar close NOT found");
    }

    // Make sidebar links active
    document.querySelectorAll('.sidebar-link').forEach(link => {
        link.addEventListener('click', function () {
            document.querySelectorAll('.sidebar-link').forEach(l => l.classList.remove('active'));
            this.classList.add('active');
        });
    });

    const path = window.location.pathname;

    document.querySelectorAll('.sidebar-link').forEach(link => {
        // So sánh đường dẫn hiện tại với href của từng link
        if (link.getAttribute('href') === path) {
            link.classList.add('active');
        } else {
            link.classList.remove('active');
        }
    });
    
    const borrowerNameInput = document.getElementById('borrowerName');
    const borrowerIdInput = document.getElementById('borrowerId');
    const borrowerSuggestions = document.getElementById('borrowerSuggestions');
    const bookNameInput = document.getElementById('bookName');
    const bookIdInput = document.getElementById('bookId');
    const bookSuggestions = document.getElementById('bookSuggestions');
    const borrowForm = document.getElementById('borrowForm');

    const searchInput = document.getElementById('searchInput');
    const statusFilter = document.getElementById('statusFilter');
    const tableBody = document.querySelector('#recordsTableBody');
    const paginationDiv = document.getElementById('pagination');

    let borrowers = [];
    let books = [];
    let currentPage = 0;
    let totalPages = 0;
    let currentStatusFilter = 'all';
    let currentSearchTerm = '';

    // ========= INIT DATA =========
    async function initData() {
        try {
            const [borrowerRes, bookRes] = await Promise.all([
                fetch('/api/borrowers'),
                fetch('/api/books')
            ]);
            borrowers = await borrowerRes.json();
            books = await bookRes.json();
        } catch (e) {
            console.error('Failed to load borrowers/books:', e);
        }
    }

    initData();

    // ========= AUTOCOMPLETE NAME -> ID =========
    borrowerNameInput.addEventListener('input', debounce(() => {
        const query = borrowerNameInput.value.toLowerCase();
        borrowerSuggestions.innerHTML = '';
        borrowerSuggestions.style.display = query ? 'block' : 'none';

        const filtered = borrowers.filter(b => b.name?.toLowerCase().includes(query));
        filtered.forEach(borrower => {
            const div = document.createElement('div');
            div.className = 'list-group-item list-group-item-action';
            div.textContent = `${borrower.name} (ID: ${borrower.id})`;
            div.onclick = () => {
                borrowerIdInput.value = borrower.id;
                borrowerNameInput.value = borrower.name;
                borrowerSuggestions.style.display = 'none';
            };
            borrowerSuggestions.appendChild(div);
        });
    }, 300));

    bookNameInput.addEventListener('input', debounce(() => {
        const query = bookNameInput.value.toLowerCase();
        bookSuggestions.innerHTML = '';
        bookSuggestions.style.display = query ? 'block' : 'none';

        const filtered = books.filter(b => b.title?.toLowerCase().includes(query));
        filtered.forEach(book => {
            const div = document.createElement('div');
            div.className = 'list-group-item list-group-item-action';
            div.textContent = `${book.title} (ID: ${book.id})`;
            div.onclick = () => {
                bookIdInput.value = book.id;
                bookNameInput.value = book.title;
                bookSuggestions.style.display = 'none';
            };
            bookSuggestions.appendChild(div);
        });
    }, 300));

    // ========= AUTOFILL NAME FROM ID =========
    borrowerIdInput.addEventListener('keyup', debounce(async () => {
        const id = borrowerIdInput.value.trim();
        if (!id) return;
        try {
            const res = await fetch(`/api/borrowers/${id}`);
            if (res.ok) {
                const data = await res.json();
                borrowerNameInput.value = data.name || '';
            }
        } catch (e) {
            console.error('Borrower ID fetch error:', e);
        }
    }, 300));

    bookIdInput.addEventListener('keyup', debounce(async () => {
        const id = bookIdInput.value.trim();
        if (!id) return;
        try {
            const res = await fetch(`/api/books/${id}`);
            if (res.ok) {
                const data = await res.json();
                bookNameInput.value = data.title || '';
            }
        } catch (e) {
            console.error('Book ID fetch error:', e);
        }
    }, 300));

    // ========= SUBMIT FORM =========
    borrowForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const borrowerId = borrowerIdInput.value;
        const bookId = bookIdInput.value;

        if (!borrowerId || !bookId) {
            alert('Please select both borrower and book.');
            return;
        }

        try {
            const res = await fetch('/api/records/borrow', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ borrowerId: parseInt(borrowerId), bookId: parseInt(bookId) })
            });

            if (res.ok) {
                alert('Book borrowed successfully!');
                borrowForm.reset();
                borrowerIdInput.value = '';
                borrowerNameInput.value = '';
                bookIdInput.value = '';
                bookNameInput.value = '';
                loadRecords();
                loadLoansToday();
            } else {
                const error = await res.text();
                alert('Error: ' + error);
            }
        } catch (err) {
            alert('Request error: ' + err.message);
        }
    });

    // ========= SUGGESTIONS HIDE =========
    document.addEventListener('click', (e) => {
        if (!borrowerSuggestions.contains(e.target) && e.target !== borrowerNameInput) {
            borrowerSuggestions.style.display = 'none';
        }
        if (!bookSuggestions.contains(e.target) && e.target !== bookNameInput) {
            bookSuggestions.style.display = 'none';
        }
    });

    // ========= LOAD LOANS TODAY =========
    async function loadLoansToday() {
        try {
            const res = await fetch('/api/records/today');
            const count = await res.json();
            const el = document.getElementById('loansToday');
            if (el) el.textContent = `Loans Today: ${count}`;
        } catch (e) {
            console.error('Error loading loans today:', e);
        }
    }

    // ========= LOAD RECORDS WITH FILTER/PAGINATION =========
    async function loadRecords(page = 0) {
        currentPage = page;
        const size = 10;
        let url = `/api/records?page=${page}&size=${size}`;
        if (currentSearchTerm) url += `&search=${encodeURIComponent(currentSearchTerm)}`;
        if (currentStatusFilter !== 'all') url += `&status=${currentStatusFilter}`;

        try {
            const res = await fetch(url);
            const data = await res.json();
            tableBody.innerHTML = '';
            totalPages = data.totalPages || 1;

            if (!data.content?.length) {
                tableBody.innerHTML = noRecordTemplate("No records found");
            } else {
                data.content.forEach(record => addRecordToTable(record));
            }

            updatePagination();
        } catch (e) {
            console.error("Load records error:", e);
            tableBody.innerHTML = noRecordTemplate("Failed to load records");
        }
    }

    // ========= ADD RECORD ROW =========
    function addRecordToTable(record) {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td class="ps-4">${record.id}</td>
            <td>${record.borrowerName}</td>
            <td>${record.bookTitle}</td>
            <td>${formatDate(record.borrowDate)}</td>
            <td>${formatDate(record.returnDate)}</td>
            <td>
                <span class="badge ${record.status === 0 ? 'bg-warning' : 'bg-success'}">
                    ${record.status === 0 ? 'Borrowed' : 'Returned'}
                </span>
            </td>
            <td class="text-end pe-4">
                ${record.status === 0 ? `
                    <button class="btn btn-sm btn-outline-success return-btn" data-id="${record.id}">
                        <iconify-icon icon="mdi:book-arrow-left" width="16" height="16"></iconify-icon> Return
                    </button>
                ` : ''}
            </td>
        `;
        tableBody.appendChild(row);

        const returnBtn = row.querySelector('.return-btn');
        if (returnBtn) {
            returnBtn.addEventListener('click', async () => {
                if (confirm('Return this book?')) {
                    const id = returnBtn.dataset.id;
                    const res = await fetch(`/api/records/return/${id}`, { method: 'POST' });
                    if (res.ok) {
                        alert('Returned!');
                        loadRecords(currentPage);
                        loadLoansToday();
                    } else {
                        alert('Error: ' + await res.text());
                    }
                }
            });
        }
    }

    // ========= PAGINATION =========
    function updatePagination() {
        paginationDiv.innerHTML = '';
        if (totalPages <= 1) return;

        const prevBtn = document.createElement('button');
        prevBtn.className = 'btn btn-outline-primary mx-1';
        prevBtn.textContent = 'Previous';
        prevBtn.disabled = currentPage === 0;
        prevBtn.onclick = () => loadRecords(currentPage - 1);

        const nextBtn = document.createElement('button');
        nextBtn.className = 'btn btn-outline-primary mx-1';
        nextBtn.textContent = 'Next';
        nextBtn.disabled = currentPage >= totalPages - 1;
        nextBtn.onclick = () => loadRecords(currentPage + 1);

        const info = document.createElement('span');
        info.className = 'mx-2';
        info.textContent = `Page ${currentPage + 1} of ${totalPages}`;

        paginationDiv.appendChild(prevBtn);
        paginationDiv.appendChild(info);
        paginationDiv.appendChild(nextBtn);
    }

    // ========= SEARCH & FILTER =========
    if (searchInput) {
        searchInput.addEventListener('input', debounce(() => {
            currentSearchTerm = searchInput.value.trim();
            loadRecords(0);
        }, 300));
    }

    if (statusFilter) {
        statusFilter.addEventListener('change', () => {
            currentStatusFilter = statusFilter.value;
            loadRecords(0);
        });
    }

    // ========= UTILITIES =========
    function formatDate(dateStr) {
        if (!dateStr) return '-';
        const date = new Date(dateStr);
        return date.toLocaleDateString('vi-VN');
    }

    function debounce(func, delay) {
        let timeout;
        return (...args) => {
            clearTimeout(timeout);
            timeout = setTimeout(() => func.apply(this, args), delay);
        };
    }

    function noRecordTemplate(msg) {
        return `
            <tr>
                <td colspan="7" class="text-center py-4">
                    <iconify-icon icon="mdi:inbox" class="fs-1 text-muted" width="40" height="40"></iconify-icon>
                    <p class="mt-2 mb-0">${msg}</p>
                </td>
            </tr>
        `;
    }

    // ========= INITIAL LOAD =========
    loadRecords();
    loadLoansToday();
});
