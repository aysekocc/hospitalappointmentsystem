Hospital Appointment System

Bu proje, hastaların randevu almasını, doktorların randevuları yönetmesini ve yöneticilerin sistemi kontrol etmesini sağlayan tam kapsamlı bir hastane randevu yönetim uygulamasıdır.

Özellikler

Kullanıcı rolleri: Hasta, Doktor
JWT tabanlı kimlik doğrulama ve rol bazlı yetkilendirme
Randevu oluşturma, listeleme, güncelleme ve iptal etme
Modern ve responsive kullanıcı arayüzü
RESTful API mimarisi

Kullanılan Teknolojiler

Backend:	Java 17, Spring Boot
Frontend:	Vue.js 3
Database:	PostgreSQL
Containerization:	Docker

Kurulum ve Çalıştırma

1️)Backend

cd backend
mvn clean install
mvn spring-boot:run

2️)Frontend

cd frontend
npm install
npm run serve

3️)Docker ile Çalıştırma (Opsiyonel)

Tüm sistemi container’lar içinde ayağa kaldırmak için:
docker-compose up --build

<img width="1878" height="889" alt="Ekran görüntüsü 2025-10-07 162948" src="https://github.com/user-attachments/assets/ee5f9587-c338-4b92-ad1c-18aa26d546b7" />
<img width="1866" height="869" alt="Ekran görüntüsü 2025-10-07 163006" src="https://github.com/user-attachments/assets/779f8619-5060-47b5-aa42-5d2764fb37ce" />
<img width="1874" height="871" alt="Ekran görüntüsü 2025-10-07 163027" src="https://github.com/user-attachments/assets/6b10d2b0-e7e2-4e5f-8235-179e7e82fa23" />
<img width="1818" height="807" alt="Ekran görüntüsü 2025-10-07 163046" src="https://github.com/user-attachments/assets/b51a4de1-8b67-4041-ad50-bbf034be8c36" />
<img width="1845" height="854" alt="Ekran görüntüsü 2025-10-07 163117" src="https://github.com/user-attachments/assets/09f87c5d-b928-4250-bac3-afeb97257a94" />
<img width="1855" height="865" alt="Ekran görüntüsü 2025-10-07 163132" src="https://github.com/user-attachments/assets/006f0723-f3d7-452f-a3cd-630ef3aae769" />
<img width="1884" height="894" alt="Ekran görüntüsü 2025-10-07 162913" src="https://github.com/user-attachments/assets/fae4d8a0-36c4-41d7-a79e-a8aae731e4bf" />
<img width="1875" height="885" alt="Ekran görüntüsü 2025-10-07 162932" src="https://github.com/user-attachments/assets/ca23a1a6-e038-462b-bf2a-10f28ec00f48" />



