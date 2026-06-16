const phones = [
  {
    name: "iPhone 14 Pro",
    price: "₴47,999",
    image:
      "https://cdn.pixabay.com/photo/2023/01/24/19/34/iphone-7741206_960_720.jpg",
    description:
      "Новий флагман від Apple з камерою 48 МП та дисплеєм Super Retina XDR.",
  },
  {
    name: "Samsung Galaxy S23",
    price: "₴42,500",
    image:
      "https://cdn.pixabay.com/photo/2023/03/01/18/41/samsung-7823899_960_720.jpg",
    description:
      "Флагман Samsung з чипом Snapdragon 8 Gen 2 та AMOLED дисплеєм.",
  },
  {
    name: "Xiaomi Redmi Note 12",
    price: "₴9,999",
    image:
      "https://cdn.pixabay.com/photo/2022/08/14/20/35/phone-7387526_960_720.jpg",
    description:
      "Бюджетна модель з великим дисплеєм, потужною батареєю та гарною камерою.",
  },
];

const phoneList = document.getElementById("phoneList");
const searchInput = document.getElementById("search");
const modal = document.getElementById("modal");
const modalTitle = document.getElementById("modalTitle");
const modalImage = document.getElementById("modalImage");
const modalDescription = document.getElementById("modalDescription");
const buySection = document.getElementById("buySection");

function renderPhones(filteredPhones) {
  phoneList.innerHTML = "";
  filteredPhones.forEach((phone) => {
    const card = document.createElement("div");
    card.className = "phone-card";
    card.innerHTML = `
      <img src="${phone.image}" alt="${phone.name}" />
      <h3>${phone.name}</h3>
      <p>${phone.price}</p>
    `;
    card.onclick = () => showModal(phone);
    phoneList.appendChild(card);
  });
}

function showModal(phone) {
  modalTitle.textContent = phone.name;
  modalImage.src = phone.image;
  modalDescription.textContent = phone.description;
  buySection.innerHTML = `<button onclick="buyPhone()">Купити</button>`;
  modal.classList.remove("hidden");
}

function closeModal() {
  modal.classList.add("hidden");
}

function buyPhone() {
  buySection.innerHTML = `
    <label>Оберіть картку:</label><br/>
    <select id="cardSelect">
      <option>Visa **** 1234</option>
      <option>Mastercard **** 5678</option>
      <option>Monobank **** 9012</option>
    </select><br/><br/>
    <button onclick="confirmPurchase()">Підтвердити</button>
  `;
}

function confirmPurchase() {
  const card = document.getElementById("cardSelect").value;
  alert(`Оплата з ${card} успішна! Дякуємо за покупку!`);
  closeModal();
}

searchInput.addEventListener("input", () => {
  const query = searchInput.value.toLowerCase();
  const filtered = phones.filter((p) => p.name.toLowerCase().includes(query));
  renderPhones(filtered);
});

renderPhones(phones);
