const container = document.querySelector(".swiper-container");
const wrapper = document.querySelector(".swiper-wrapper");
const slides = document.querySelectorAll(".swiper-slide");
const prevButton = document.getElementById("prev-button");
const nextButton = document.getElementById("next-button");
let currentIndex = 0;

function goToSlide(index) {
    if (index < 0) {
        index = slides.length - 1;
    } else if (index >= slides.length) {
        index = 0;
    }
    currentIndex = index;
    const offset = -currentIndex * 100 + "%";
    wrapper.style.transform = `translateX(${offset})`;
}

prevButton.addEventListener("click", () => {
    goToSlide(currentIndex - 1);
});

nextButton.addEventListener("click", () => {
    goToSlide(currentIndex + 1);
});

container.addEventListener("touchstart", (e) => {
    const startX = e.touches[0].clientX;
    container.addEventListener("touchend", function onTouchEnd(e) {
        const endX = e.changedTouches[0].clientX;
        if (startX - endX > 50) {
            goToSlide(currentIndex + 1);
        } else if (endX - startX > 50) {
            goToSlide(currentIndex - 1);
        }
        container.removeEventListener("touchend", onTouchEnd);
    });
});

goToSlide(currentIndex);
