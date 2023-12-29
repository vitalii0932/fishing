window.onload = function () {
    showTable('products');
};

function showOrders() {
    fetch(`/adminData/getTableData/orders`)
        .then(response => response.json())
        .then(data => {
            let tableContainer = document.getElementById('tableContainer');
            tableContainer.innerHTML = ''; // Clear previous content
            let table = document.createElement('table');
            let thead = document.createElement('thead');
            let tbody = document.createElement('tbody');

            let headerRow = document.createElement('tr');
            let th = document.createElement('th');
            th.textContent = 'Order id';
            headerRow.appendChild(th);

            th = document.createElement('th');
            th.textContent = 'User name';
            headerRow.appendChild(th);

            th = document.createElement('th');
            th.textContent = 'Phone number';
            headerRow.appendChild(th);

            th = document.createElement('th');
            th.textContent = 'Call';
            headerRow.appendChild(th);

            th = document.createElement('th');
            th.textContent = 'Town';
            headerRow.appendChild(th);

            th = document.createElement('th');
            th.textContent = 'Post index';
            headerRow.appendChild(th);

            th = document.createElement('th');
            th.textContent = 'Products';
            headerRow.appendChild(th);

            th = document.createElement('th');
            th.textContent = 'Status';
            headerRow.appendChild(th);

            th = document.createElement('th');
            th.textContent = 'Edit status';
            headerRow.appendChild(th);

            thead.appendChild(headerRow);

            data.forEach(rowData => {
                let parts = rowData.split(";");
                let row = document.createElement('tr');
                parts.forEach(result => {
                    let cell = document.createElement('td');
                    cell.textContent = result;
                    row.appendChild(cell);
                });

                let editButtonCell = document.createElement('td');
                let editButton = document.createElement('button');
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
        });
}

function showTable(tableName) {
    fetch(`/adminData/getTableData/${tableName}`)
        .then(response => response.json())
        .then(data => {
            let tableContainer = document.getElementById('tableContainer');
            tableContainer.innerHTML = ''; // Clear previous content

            let table = document.createElement('table');
            let thead = document.createElement('thead');
            let tbody = document.createElement('tbody');

            let headers = Object.keys(data[0]);

            let headerRow = document.createElement('tr');
            headers.forEach(header => {
                let th = document.createElement('th');
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

            if (tableName === 'users') {
                headerRow.innerHTML += '<th>Заблокувати</th><th>Видалити</th>';
            } else {
                headerRow.innerHTML += '<th>Змінити</th><th>Видалити</th>';
            }
            thead.appendChild(headerRow);

            data.forEach(rowData => {
                let row = document.createElement('tr');
                headers.forEach(header => {
                    let cell = document.createElement('td');
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

                let editButtonCell = document.createElement('td');
                let editButton = document.createElement('button');
                if (tableName === 'users') {
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

                let deleteButtonCell = document.createElement('td');
                let deleteButton = document.createElement('button');
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
