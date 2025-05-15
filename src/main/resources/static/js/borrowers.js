document.addEventListener('DOMContentLoaded', function () {
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
        if (link.getAttribute('href') === path) {
            link.classList.add('active');
        } else {
            link.classList.remove('active');
        }
    });

    // Borrower management functionality
    const searchInput = document.getElementById('searchInput');
    const borrowCountFilter = document.getElementById('borrowCountFilter');
    const resetFiltersBtn = document.getElementById('resetFilters');
    const tableViewBtn = document.getElementById('tableViewBtn');
    const gridViewBtn = document.getElementById('gridViewBtn');
    const tableBody = document.querySelector('tbody');
    const gridView = document.querySelector('.grid-view');
    const entriesText = document.querySelector('.card-footer .text-muted span');

    // Only initialize borrower-related features if we're on the borrowers page
    if (tableBody && gridView) {
        // Debounce function to optimize search input
        function debounce(func, wait) {
            let timeout;
            return function (...args) {
                clearTimeout(timeout);
                timeout = setTimeout(() => func.apply(this, args), wait);
            };
        }

        // Search and Filter functionality
        function filterBorrowers() {
            const searchTerm = searchInput?.value.toLowerCase() || '';
            const borrowCountStatus = borrowCountFilter?.value || 'all';

            // Filter table rows
            const rows = tableBody.querySelectorAll('tr');
            let visibleCount = 0;

            rows.forEach(row => {
                const td = row.querySelector('td');
                if (!td) return; // Skip "no borrowers found" row

                const id = row.querySelector('td:nth-child(1)')?.textContent.toLowerCase() || '';
                const name = row.querySelector('td:nth-child(2)')?.textContent.toLowerCase() || '';
                const email = row.querySelector('td:nth-child(3)')?.textContent.toLowerCase() || '';
                const phone = row.querySelector('td:nth-child(4)')?.textContent.toLowerCase() || '';
                const borrowCount = parseInt(row.querySelector('td:nth-child(5)')?.textContent) || 0;

                const matchesSearch = id.includes(searchTerm) || 
                                     name.includes(searchTerm) || 
                                     email.includes(searchTerm) || 
                                     phone.includes(searchTerm);
                const matchesBorrowCount = borrowCountStatus === 'all' || 
                                          (borrowCountStatus === 'borrowed' && borrowCount > 0) || 
                                          (borrowCountStatus === 'notBorrowed' && borrowCount === 0);

                if (matchesSearch && matchesBorrowCount) {
                    row.style.display = '';
                    visibleCount++;
                } else {
                    row.style.display = 'none';
                }
            });

            // Filter grid items
            const gridItems = gridView.querySelectorAll('.col-md-4');
            gridItems.forEach(item => {
                const name = item.querySelector('h5')?.textContent.toLowerCase() || '';
                const email = item.querySelector('p:nth-child(2) span')?.textContent.toLowerCase() || '';
                const phone = item.querySelector('p:nth-child(3) span')?.textContent.toLowerCase() || '';
                const borrowCount = parseInt(item.querySelector('p:nth-child(4) span')?.textContent) || 0;

                const matchesSearch = name.includes(searchTerm) || 
                                     email.includes(searchTerm) || 
                                     phone.includes(searchTerm);
                const matchesBorrowCount = borrowCountStatus === 'all' || 
                                          (borrowCountStatus === 'borrowed' && borrowCount > 0) || 
                                          (borrowCountStatus === 'notBorrowed' && borrowCount === 0);

                if (matchesSearch && matchesBorrowCount) {
                    item.style.display = '';
                } else {
                    item.style.display = 'none';
                }
            });

            // Update entries text
            if (entriesText) {
                entriesText.textContent = visibleCount;
            }
        }

        // Event Listeners
        if (searchInput) {
            searchInput.addEventListener('input', debounce(filterBorrowers, 300));
        }

        if (borrowCountFilter) {
            borrowCountFilter.addEventListener('change', filterBorrowers);
        }

        if (resetFiltersBtn) {
            resetFiltersBtn.addEventListener('click', () => {
                if (searchInput) searchInput.value = '';
                if (borrowCountFilter) borrowCountFilter.value = 'all';
                filterBorrowers();
            });
        }

        // View Toggle
        if (tableViewBtn && gridViewBtn) {
            tableViewBtn.addEventListener('click', () => {
                document.querySelector('.table-responsive').style.display = 'block';
                gridView.style.display = 'none';
                tableViewBtn.classList.add('active');
                gridViewBtn.classList.remove('active');
                filterBorrowers(); // Re-apply filters
            });

            gridViewBtn.addEventListener('click', () => {
                document.querySelector('.table-responsive').style.display = 'none';
                gridView.style.display = 'block';
                tableViewBtn.classList.remove('active');
                gridViewBtn.classList.add('active');
                filterBorrowers(); // Re-apply filters
            });
        }
    }
});