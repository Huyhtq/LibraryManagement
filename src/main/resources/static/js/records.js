document.addEventListener('DOMContentLoaded', () => {
    console.log('Records page loaded');

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

    // Handle Borrow Form Submission with AJAX
    const borrowForm = document.getElementById('borrowForm');
    if (borrowForm) {
        borrowForm.addEventListener('submit', async (event) => {
            event.preventDefault();

            const formData = new FormData(borrowForm);
            const response = await fetch('/api/records/borrow', {
                method: 'POST',
                body: formData
            });

            if (response.ok) {
                const data = await response.json();
                alert('Book borrowed successfully!');
                addRecordToTable(data);
                borrowForm.reset();
            } else {
                const error = await response.text();
                alert('Error: ' + error);
            }
        });
    }

    // Handle Return Button Click with AJAX
    const returnButtons = document.querySelectorAll('button[th:onclick^="window.location.href"]');
    returnButtons.forEach(button => {
        button.addEventListener('click', async (event) => {
            event.preventDefault();
            if (confirm('Are you sure you want to return this book?')) {
                const recordId = button.getAttribute('th:onclick').match(/\/return\/(\d+)/)[1];
                const response = await fetch(`/api/records/return/${recordId}`, {
                    method: 'POST'
                });

                if (response.ok) {
                    alert('Book returned successfully!');
                    removeRecordFromTable(recordId);
                } else {
                    const error = await response.text();
                    alert('Error: ' + error);
                }
            }
        });
    });

    // Load initial records
    loadRecords();

    // Handle Search and Filter
    const searchInput = document.getElementById('searchInput');
    const statusFilter = document.getElementById('statusFilter');
    if (searchInput || statusFilter) {
        const debouncedFilter = debounce(filterRecords, 300);
        searchInput?.addEventListener('input', debouncedFilter);
        statusFilter?.addEventListener('change', debouncedFilter);
    }
});

// Load records from API
async function loadRecords() {
    const response = await fetch('/api/records');
    if (response.ok) {
        const records = await response.json();
        const tbody = document.getElementById('recordsTableBody');
        tbody.innerHTML = ''; // Clear existing content
        if (records.length === 0) {
            tbody.innerHTML = `
                <tr>
                    <td colspan="7" class="text-center py-4">
                        <iconify-icon icon="mdi:inbox" class="fs-1 text-muted" width="40" height="40"></iconify-icon>
                        <p class="mt-2 mb-0">No records found</p>
                    </td>
                </tr>
            `;
        } else {
            records.forEach(record => addRecordToTable(record));
        }
    }
}

// Debounce function to limit search calls
function debounce(func, wait) {
    let timeout;
    return function (...args) {
        clearTimeout(timeout);
        timeout = setTimeout(() => func.apply(this, args), wait);
    };
}

// Filter records
function filterRecords() {
    const searchTerm = (document.getElementById('searchInput') || {}).value?.toLowerCase() || '';
    const status = (document.getElementById('statusFilter') || {}).value || 'all';
    const rows = document.querySelectorAll('#recordsTableBody tr');

    rows.forEach(row => {
        const borrower = row.cells[1].textContent.toLowerCase();
        const book = row.cells[2].textContent.toLowerCase();
        const statusText = row.cells[5].textContent.toLowerCase();
        const matchesSearch = borrower.includes(searchTerm) || book.includes(searchTerm);
        const matchesStatus = status === 'all' || statusText.includes(status === '0' ? 'borrowed' : 'returned');

        row.style.display = matchesSearch && matchesStatus ? '' : 'none';
    });
}

// Add record to table
function addRecordToTable(record) {
    const tbody = document.getElementById('recordsTableBody');
    const row = document.createElement('tr');
    row.innerHTML = `
        <td class="ps-4">${record.id}</td>
        <td>${record.borrower.name}</td>
        <td>${record.book.title}</td>
        <td>${record.borrow_date ? new Date(record.borrow_date).toLocaleDateString('vi-VN', { day: '2-digit', month: '2-digit', year: 'numeric' }) : '-'}</td>
        <td>${record.return_date ? new Date(record.return_date).toLocaleDateString('vi-VN', { day: '2-digit', month: '2-digit', year: 'numeric' }) : '-'}</td>
        <td>
            <span class="badge ${record.status === 0 ? 'bg-warning' : 'bg-success'}">${record.status === 0 ? 'Borrowed' : 'Returned'}</span>
        </td>
        <td class="text-end pe-4">
            ${record.status === 0 ? `
                <button class="btn btn-sm btn-outline-success return-btn" data-id="${record.id}">
                    <iconify-icon icon="mdi:book-arrow-left" width="16" height="16"></iconify-icon> Return
                </button>
            ` : ''}
        </td>
    `;
    tbody.appendChild(row);

    // Attach event listener to the return button
    const returnButton = row.querySelector('.return-btn');
    if (returnButton) {
        returnButton.addEventListener('click', async () => {
            if (confirm('Are you sure you want to return this book?')) {
                const recordId = returnButton.getAttribute('data-id');
                const response = await fetch(`/api/records/return/${recordId}`, {
                    method: 'POST'
                });

                if (response.ok) {
                    alert('Book returned successfully!');
                    removeRecordFromTable(recordId);
                } else {
                    const error = await response.text();
                    alert('Error: ' + error);
                }
            }
        });
    }
}

// Remove record from table dynamically
function removeRecordFromTable(recordId) {
    const row = document.querySelector(`table tbody tr td:first-child:contains(${recordId})`)?.parentElement;
    if (row) row.remove();
}

// Helper function to simulate returnBook (if needed)
function returnBook(id) {
    fetch(`/records/return/${id}`, { method: 'GET' })
        .then(response => response.ok ? alert('Book returned!') : alert('Error!'))
        .then(() => location.reload())
        .catch(error => alert('Error: ' + error));
}

async function loadOverdueRecords(page = 0, size = 10) {
    const response = await fetch(`/api/records/overdue?page=${page}&size=${size}`);
    if (response.ok) {
        const data = await response.json();
        const tbody = document.getElementById('recordsTableBody');
        tbody.innerHTML = '';
        if (data.content.length === 0) {
            tbody.innerHTML = `
                <tr>
                    <td colspan="7" class="text-center py-4">
                        <iconify-icon icon="mdi:inbox" class="fs-1 text-muted" width="40" height="40"></iconify-icon>
                        <p class="mt-2 mb-0">No overdue records found</p>
                    </td>
                </tr>
            `;
        } else {
            data.content.forEach(record => addRecordToTable(record));
        }
    } else {
        console.error('Failed to load overdue records:', response.statusText);
    }
}