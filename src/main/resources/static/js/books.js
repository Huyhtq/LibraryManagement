document.addEventListener('DOMContentLoaded', function () {
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

    // Book management functionality
    const searchInput = document.getElementById('searchInput');
    const categoryFilter = document.getElementById('categoryFilter');
    const statusFilter = document.getElementById('statusFilter');
    const resetFiltersBtn = document.getElementById('resetFilters');
    const tableViewBtn = document.getElementById('tableViewBtn');
    const gridViewBtn = document.getElementById('gridViewBtn');
    const tableBody = document.querySelector('tbody');
    const gridView = document.querySelector('.grid-view');
    const entriesText = document.querySelector('.card-footer .text-muted span');

    // Only initialize book-related features if we're on the books page
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
        function filterBooks() {
            const searchTerm = searchInput?.value.toLowerCase() || '';
            const category = categoryFilter?.value || 'all';
            const status = statusFilter?.value || 'all';

            // Filter table rows
            const rows = tableBody.querySelectorAll('tr');
            let visibleCount = 0;

            rows.forEach(row => {
                const td = row.querySelector('td');
                if (!td) return; // Skip "no books found" row

                const id = row.querySelector('td:nth-child(1)')?.textContent.toLowerCase() || '';
                const title = row.querySelector('td:nth-child(2)')?.textContent.toLowerCase() || '';
                const author = row.querySelector('td:nth-child(3)')?.textContent.toLowerCase() || '';
                const quantity = parseInt(row.querySelector('td:nth-child(4)')?.textContent) || 0;

                const matchesSearch = id.includes(searchTerm) || title.includes(searchTerm) || author.includes(searchTerm);
                const matchesCategory = category === 'all'; // Add category logic if implemented
                const matchesStatus = status === 'all' || 
                                     (status === 'instock' && quantity > 0) || 
                                     (status === 'outofstock' && quantity === 0);

                if (matchesSearch && matchesCategory && matchesStatus) {
                    row.style.display = '';
                    visibleCount++;
                } else {
                    row.style.display = 'none';
                }
            });

            // Filter grid items
            const gridItems = gridView.querySelectorAll('.col-md-4');
            gridItems.forEach(item => {
                const title = item.querySelector('h5')?.textContent.toLowerCase() || '';
                const author = item.querySelector('p:nth-child(2) span')?.textContent.toLowerCase() || '';
                const quantity = parseInt(item.querySelector('p:nth-child(3) span')?.textContent) || 0;

                const matchesSearch = title.includes(searchTerm) || author.includes(searchTerm);
                const matchesCategory = category === 'all';
                const matchesStatus = status === 'all' || 
                                     (status === 'instock' && quantity > 0) || 
                                     (status === 'outofstock' && quantity === 0);

                if (matchesSearch && matchesCategory && matchesStatus) {
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
            searchInput.addEventListener('input', debounce(filterBooks, 300));
        }

        if (categoryFilter) {
            categoryFilter.addEventListener('change', filterBooks);
        }

        if (statusFilter) {
            statusFilter.addEventListener('change', filterBooks);
        }

        if (resetFiltersBtn) {
            resetFiltersBtn.addEventListener('click', () => {
                if (searchInput) searchInput.value = '';
                if (categoryFilter) categoryFilter.value = 'all';
                if (statusFilter) statusFilter.value = 'all';
                filterBooks();
            });
        }

        // View Toggle
        if (tableViewBtn && gridViewBtn) {
            tableViewBtn.addEventListener('click', () => {
                document.querySelector('.table-responsive').style.display = 'block';
                gridView.style.display = 'none';
                tableViewBtn.classList.add('active');
                gridViewBtn.classList.remove('active');
                filterBooks(); // Re-apply filters
            });

            gridViewBtn.addEventListener('click', () => {
                document.querySelector('.table-responsive').style.display = 'none';
                gridView.style.display = 'block';
                tableViewBtn.classList.remove('active');
                gridViewBtn.classList.add('active');
                filterBooks(); // Re-apply filters
            });
        }
    }
});