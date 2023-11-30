window.onload = function () {
    showTable('products');
};

function showOrders() {
    fetch(`/adminData/getTableData/orders`)
        .then(response => response.json())
        .then(data => {
            var tableContainer = document.getElementById('tableContainer');
            tableContainer.innerHTML = ''; // Clear previous content
            var table = document.createElement('table');
            var thead = document.createElement('thead');
            var tbody = document.createElement('tbody');

            var headerRow = document.createElement('tr');
            var th = document.createElement('th');
            th.textContent = 'Order id';
            headerRow.appendChild(th);

            var th = document.createElement('th');
            th.textContent = 'User name';
            headerRow.appendChild(th);

            var th = document.createElement('th');
            th.textContent = 'Phone number';
            headerRow.appendChild(th);

            var th = document.createElement('th');
            th.textContent = 'Call';
            headerRow.appendChild(th);

            var th = document.createElement('th');
            th.textContent = 'Town';
            headerRow.appendChild(th);

            var th = document.createElement('th');
            th.textContent = 'Post index';
            headerRow.appendChild(th);

            var th = document.createElement('th');
            th.textContent = 'Products';
            headerRow.appendChild(th);

            var th = document.createElement('th');
            th.textContent = 'Status';
            headerRow.appendChild(th);

            var th = document.createElement('th');
            th.textContent = 'Edit status';
            headerRow.appendChild(th);

            thead.appendChild(headerRow);

            data.forEach(rowData => {
                let parts = rowData.split(";");

                var row = document.createElement('tr');
                parts.forEach(result => {
                    var cell = document.createElement('td');
                    cell.textContent = result; // Заменил rowData на result
                    row.appendChild(cell);
                });

                var editButtonCell = document.createElement('td');
                var editButton = document.createElement('button');
                editButton.textContent = 'Edit status';
                editButton.className = 'edit-button';
                editButton.addEventListener('click', function () {
                    editStatus(rowData.split(";")[0]);
                });
                editButtonCell.appendChild(editButton);
                row.appendChild(editButtonCell);

                tbody.appendChild(row);
            });

            table.appendChild(thead);
            table.appendChild(tbody);
            tableContainer.appendChild(table);
        })
}

function showTable(tableName) {
    fetch(`/adminData/getTableData/${tableName}`)
        .then(response => response.json())
        .then(data => {
            var tableContainer = document.getElementById('tableContainer');
            tableContainer.innerHTML = ''; // Clear previous content

            var table = document.createElement('table');
            var thead = document.createElement('thead');
            var tbody = document.createElement('tbody');

            var headers = Object.keys(data[0]);

            var headerRow = document.createElement('tr');
            headers.forEach(header => {
                var th = document.createElement('th');
                if (header === 'type') {
                    th.textContent = 'Type ID';
                    headerRow.appendChild(th);
                    th = document.createElement('th');
                    th.textContent = 'Type Name';
                } else {
                    th.textContent = header;
                }
                headerRow.appendChild(th);
            });
            if(tableName == 'users') {
                headerRow.innerHTML += '<th>Заблокувати</th><th>Видалити</th>';
            } else {
                headerRow.innerHTML += '<th>Змінити</th><th>Видалити</th>';
            }
            thead.appendChild(headerRow);

            data.forEach(rowData => {
                var row = document.createElement('tr');
                headers.forEach(header => {
                    var cell = document.createElement('td');
                    if (header === 'type') {
                        cell.textContent = rowData.type.id;
                        row.appendChild(cell);
                        cell = document.createElement('td');
                        cell.textContent = rowData.type.name;
                    } else {
                        cell.textContent = rowData[header];
                    }
                    row.appendChild(cell);
                });

                var editButtonCell = document.createElement('td');
                var editButton = document.createElement('button');
                if(tableName == 'users') {
                    editButton.textContent = 'Заблокувати';
                } else {
                    editButton.textContent = 'Змінити';
                }

                editButton.className = 'edit-button';
                editButton.addEventListener('click', function () {
                    showEditForm(tableName, rowData.id);
                });
                editButtonCell.appendChild(editButton);
                row.appendChild(editButtonCell);

                var deleteButtonCell = document.createElement('td');
                var deleteButton = document.createElement('button');
                deleteButton.textContent = 'Видалити';
                deleteButton.className = 'delete-button';
                deleteButton.addEventListener('click', function () {
                    deleteById(tableName, rowData.id);
                });
                deleteButtonCell.appendChild(deleteButton);
                row.appendChild(deleteButtonCell);

                tbody.appendChild(row);
            });

            table.appendChild(thead);
            table.appendChild(tbody);
            tableContainer.appendChild(table);
        })
        .catch(error => console.error('Error:', error));
}

function showEditForm(table, id) {
    window.location.href = `/admin/update/${table}/${id}`;
}

function deleteById(table, id) {
    window.location.href = `/admin/delete/${table}/${id}`;
}

function addProduct(table) {
    window.location.href = `/admin/update/${table}/add`
}

function editStatus(status) {
    window.location.href = `/admin/editStatus/${status}`
}