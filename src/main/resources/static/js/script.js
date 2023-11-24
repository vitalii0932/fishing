function viewDivDiscounts(){
    document.getElementById("product-hits").style.display = "none";
    document.getElementById("product-discounts").style.display = "block";
};

function viewDivHits(){
    document.getElementById("product-discounts").style.display = "none";
    document.getElementById("product-hits").style.display = "block";
};

const button1 = document.getElementById('buttonDisc');
const button2 = document.getElementById('buttonHits');

button1.addEventListener('click', function() {
    button1.classList.add('active');
    button2.classList.remove('active');
});

button2.addEventListener('click', function() {
    button2.classList.add('active');
    button1.classList.remove('active');
});