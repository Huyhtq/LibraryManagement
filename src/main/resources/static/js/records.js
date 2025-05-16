document.addEventListener('DOMContentLoaded', () => {
    console.log("DOM loaded");

    // Toggle sidebar
    const sidebarToggle = document.getElementById('sidebarToggle');
    const sidebarClose = document.getElementById('sidebarclose');
    const sidebar = document.getElementById('sidebar');
    const content = document.getElementById('content');

    if (sidebarToggle) {
        console.log("Sidebar toggle found");
        sidebarToggle.addEventListener('click', () => {
            console.log("Toggle clicked");
            sidebar.classList.toggle('hidden');
            content.classList.toggle('expanded');
        });
    } else {
        console.log("Sidebar toggle NOT found");
    }

    if (sidebarClose) {
        console.log("Sidebar close found");
        sidebarClose.addEventListener('click', () => {
            console.log("Close clicked");
            sidebar.classList.toggle('hidden');
            content.classList.toggle('expanded');
        });
    } else {
        console.log("Sidebar close NOT found");
    }

    // Make sidebar links active
    document.querySelectorAll('.sidebar-link').forEach(link => {
        link.addEventListener('click', () => {
            document.querySelectorAll('.sidebar-link').forEach(l => l.classList.remove('active'));
            link.classList.add('active');
        });
    });

    const path = window.location.pathname;
    document.querySelectorAll('.sidebar-link').forEach(link => {
        if (link.getAttribute('href') === path) {
            link.classList.add('active');
        } else {
            link.classList.remove('active');
        }
    });

    // Handle Borrow Form Submission
    const borrowForm = document.getElementById('borrowForm');
    if (borrowForm) {
        borrowForm.addEventListener('submit', async (event) => {
            event.preventDefault();
            const formData = new FormData(borrowForm);
            const borrowerId = formData.get('borrowerId');
            const bookId = formData.get('bookId');

            if (!borrowerId || !bookId) {
                alert('Please select both borrower and book.');
                return;
            }

            const response = await fetch('/api/records/borrow', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ borrowerId, bookId })
            });

            if (response.ok) {
                alert('Book borrowed successfully!');
                loadRecords(); // Tải lại danh sách
                loadLoansToday(); // Cập nhật số lượng mượn hôm nay
                borrowForm.reset();
            } else {
                const error = await response.text();
                alert('Error: ' + error);
            }
        });
    }

    // Record management functionality
    const searchInput = document.getElementById('searchInput');
    const statusFilter = document.getElementById('statusFilter');
    const tableBody = document.querySelector('#recordsTableBody');
    const paginationDiv = document.createElement('div'); // Thêm div cho phân trang
    paginationDiv.className = 'pagination mt-3 text-center';
    tableBody.parentElement.appendChild(paginationDiv);

    let currentPage = 0;
    let totalPages = 0;
    let currentStatusFilter = 'all';
    let currentSearchTerm = '';

    // Load loans today count
    async function loadLoansToday() {
        try {
            const response = await fetch('/api/records/today');
            if (response.ok) {
                const count = await response.json();
                const loansTodayElement = document.getElementById('loansToday');
                if (loansTodayElement) {
                    loansTodayElement.textContent = `Loans Today: ${count}`;
                } else {
                    const header = document.querySelector('.card-header h5');
                    if (header) {
                        const countSpan = document.createElement('span');
                        countSpan.id = 'loansToday';
                        countSpan.className = 'ms-3 text-muted';
                        countSpan.textContent = `Loans Today: ${count}`;
                        header.appendChild(countSpan);
                    }
                }
            } else {
                const error = await response.text();
                console.error("Failed to load loans today:", error);
                alert('Error loading loans today: ' + error);
            }
        } catch (e) {
            console.error("Fetch error:", e);
            alert('Error loading loans today: ' + e.message);
        }
    }

    // Load records with pagination, search, and status filter
    async function loadRecords(page = 0) {
        currentPage = page;
        const size = 10;
        let url = `/api/records?page=${page}&size=${size}`;

        if (currentSearchTerm) {
            url += `&search=${encodeURIComponent(currentSearchTerm)}`;
        }
        if (currentStatusFilter !== 'all') {
            url += `&status=${currentStatusFilter}`;
        }

        try {
            const response = await fetch(url);
            if (response.ok) {
                const data = await response.json();
                tableBody.innerHTML = '';
                totalPages = data.totalPages || 1;

                if (!data.content || data.content.length === 0) {
                    tableBody.innerHTML = noRecordTemplate("No records found");
                } else {
                    data.content.forEach(record => addRecordToTable(record));
                }

                updatePagination();
            } else {
                const error = await response.text();
                alert('Error loading records: ' + error);
                tableBody.innerHTML = noRecordTemplate("Failed to load records");
            }
        } catch (e) {
            console.error("Fetch error:", e);
            alert('Error loading records: ' + e.message);
            tableBody.innerHTML = noRecordTemplate("Failed to load records");
        }
    }

    // Load overdue records
    async function loadOverdueRecords(page = 0) {
        const size = 10;
        const response = await fetch(`/api/records/overdue?page=${page}&size=${size}`);
        if (response.ok) {
            const data = await response.json();
            tableBody.innerHTML = '';
            totalPages = data.totalPages || 1;

            if (!data.content || data.content.length === 0) {
                tableBody.innerHTML = noRecordTemplate("No overdue records found");
            } else {
                data.content.forEach(record => addRecordToTable(record));
            }

            updatePagination();
        } else {
            console.error("Failed to load overdue records:", response.statusText);
        }
    }

    // Add record row to table
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

        const returnButton = row.querySelector('.return-btn');
        if (returnButton) {
            returnButton.addEventListener('click', async () => {
                if (confirm('Are you sure you want to return this book?')) {
                    const id = returnButton.getAttribute('data-id');
                    const response = await fetch(`/api/records/return/${id}`, {
                        method: 'POST'
                    });

                    if (response.ok) {
                        alert('Book returned successfully!');
                        row.remove();
                        loadRecords(currentPage);
                        loadLoansToday();
                    } else {
                        const error = await response.text();
                        alert('Error: ' + error);
                    }
                }
            });
        }
    }

    // Update pagination controls
    function updatePagination() {
        paginationDiv.innerHTML = '';
        if (totalPages <= 1) return;

        const prevButton = document.createElement('button');
        prevButton.className = 'btn btn-outline-primary mx-1';
        prevButton.textContent = 'Previous';
        prevButton.disabled = currentPage === 0;
        prevButton.addEventListener('click', () => loadRecords(currentPage - 1));
        paginationDiv.appendChild(prevButton);

        const pageInfo = document.createElement('span');
        pageInfo.className = 'mx-2';
        pageInfo.textContent = `Page ${currentPage + 1} of ${totalPages}`;
        paginationDiv.appendChild(pageInfo);

        const nextButton = document.createElement('button');
        nextButton.className = 'btn btn-outline-primary mx-1';
        nextButton.textContent = 'Next';
        nextButton.disabled = currentPage >= totalPages - 1;
        nextButton.addEventListener('click', () => loadRecords(currentPage + 1));
        paginationDiv.appendChild(nextButton);
    }

    // Event listeners for search and filter
    if (searchInput) {
        searchInput.addEventListener('input', debounce(() => {
            currentSearchTerm = searchInput.value.toLowerCase();
            loadRecords(0); // Reset về trang đầu khi tìm kiếm
        }, 300));
    }

    if (statusFilter) {
        statusFilter.addEventListener('change', () => {
            currentStatusFilter = statusFilter.value;
            loadRecords(0); // Reset về trang đầu khi lọc
        });
    }

    // Initial load
    loadRecords();
    loadLoansToday();
});

// Utilities
function formatDate(dateStr) {
    if (!dateStr) return '-';
    const date = new Date(dateStr);
    return date.toLocaleDateString('vi-VN', { day: '2-digit', month: '2-digit', year: 'numeric' });
}

function debounce(func, wait) {
    let timeout;
    return function (...args) {
        clearTimeout(timeout);
        timeout = setTimeout(() => func.apply(this, args), wait);
    };
}

function noRecordTemplate(message) {
    return `
        <tr>
            <td colspan="7" class="text-center py-4">
                <iconify-icon icon="mdi:inbox" class="fs-1 text-muted" width="40" height="40"></iconify-icon>
                <p class="mt-2 mb-0">${message}</p>
            </td>
        </tr>
    `;
}