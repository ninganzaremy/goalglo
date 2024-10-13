
# GoalGlo Application

## Introduction

Building GoalGlo, a comprehensive full-stack application engineered with Java Spring Boot for the backend and React for
the
frontend. The entire application is containerized using Docker. Images are stored in Amazon Elastic Container Registry (
ECR) and deployed to Amazon Elastic Container Service (ECS) for scalable backend operations. The frontend is deployed to
Amazon S3 for high availability and performance. Continuous integration and deployment (CI/CD) are orchestrated through
Jenkins, ensuring a robust and automated deployment pipeline.

## Features

- Real-time Notifications
- Social Media Integration
- Gamification Elements
- AI-Powered Financial Advisor
- Video Consultation Feature
- Multi-language Support
- Progressive Web App (PWA) Capabilities
- Data Visualization DashboardPage
- Collaborative Goal Setting
- Marketplace for Financial Products
- User Authentication and Authorization
- BlogPage System with CRUD operations
- Email Collection and Newsletter Integration
- Appointment Scheduling
- User DashboardPage
- Admin Panel

## Technologies Used
### Frontend
- React
- Redux
- React Router
- Vite

### Backend
- Java
- Spring Boot
- Spring Security
- PostgreSQL

### Deployment

- AWS (ECR, ECS, S3, CloudFront)

## Getting Started

### Prerequisites
- Node.js and npm
- Java Development Kit (JDK)
- PostgreSQL
- AWS Account

### Installation

1. **Clone the repository:**
   \`\`\`bash
   git clone https://github.com/ninganzaremy/goalglo.git
   cd goalglo
   \`\`\`

2. **Frontend Setup:**
   \`\`\`bash
   cd frontend
   npm install
   npm run dev
   \`\`\`

3. **Backend Setup:**
   - Navigate back to the root directory:
     \`\`\`bash
     cd ..
     \`\`\`
   - Set up your PostgreSQL database and update the \`application.properties\` file with your database credentials.
   - Run the Spring Boot application:
     \`\`\`bash
     ./mvnw spring-boot:run
     \`\`\`

### Usage
- Open your browser and navigate to \`http://localhost:3000\` to view the frontend.
- Access the backend APIs at \`http://localhost:8080\`.

### Project Structure

```
goalglo/
├── frontend/
│   ├── public/
│   │   ├── index.html
│   │   ├── favicon.ico
│   │   ├── manifest.json
│   │   └── service-worker.js
│   ├── src/
│   │   ├── assets/
│   │   │   ├── images/
│   │   │   ├── fonts/
│   │   │   ├── icons/
│   │   │   └── locales/
│   │   ├── components/
│   │   │   ├── common/
│   │   │   ├── layout/
│   │   │   ├── home/
│   │   │   ├── services/
│   │   │   ├── blog/
│   │   │   ├── appointments/
│   │   │   ├── auth/
│   │   │   ├── admin/
│   │   │   ├── notifications/
│   │   │   │   ├── NotificationCenter.jsx
│   │   │   │   └── NotificationItem.jsx
│   │   │   ├── social/
│   │   │   │   ├── SocialShare.jsx
│   │   │   │   └── SocialFeed.jsx
│   │   │   ├── gamification/
│   │   │   │   ├── AchievementBadge.jsx
│   │   │   │   └── ProgressTracker.jsx
│   │   │   ├── ai-advisor/
│   │   │   │   ├── AdvisorChat.jsx
│   │   │   │   └── RecommendationCard.jsx
│   │   │   ├── video-consultation/
│   │   │   │   ├── VideoRoom.jsx
│   │   │   │   └── CallControls.jsx
│   │   │   ├── data-visualization/
│   │   │   │   ├── FinancialChart.jsx
│   │   │   │   └── GoalProgressChart.jsx
│   │   │   ├── collaborative-goals/
│   │   │   │   ├── GoalBoard.jsx
│   │   │   │   └── GoalCard.jsx
│   │   │   └── marketplace/
│   │   │       ├── ProductList.jsx
│   │   │       └── ProductCard.jsx
│   │   ├── pages/
│   │   │   ├── HomePage.jsx
│   │   │   ├── ServicesPage.jsx
│   │   │   ├── BlogPage.jsx
│   │   │   ├── AppointmentPage.jsx
│   │   │   ├── Login.jsx
│   │   │   ├── Register.jsx
│   │   │   ├── AdminDashboard.jsx
│   │   │   ├── UserDashboard.jsx
│   │   │   ├── VideoConsultationPage.jsx
│   │   │   ├── AIAdvisorPage.jsx
│   │   │   ├── MarketplacePage.jsx
│   │   │   └── CollaborativeGoalsPage.jsx
│   │   ├── redux/
│   │   │   ├── actions/
│   │   │   ├── reducers/
│   │   │   ├── slices/
│   │   │   └── store.js
│   │   ├── services/
│   │   │   ├── api.js
│   │   │   ├── socket.js
│   │   │   ├── i18n.js
│   │   │   └── aiService.js
│   │   ├── styles/
│   │   ├── utils/
│   │   ├── hooks/
│   │   ├── context/
│   │   │   ├── ThemeContext.js
│   │   │   └── LanguageContext.js
│   │   ├── App.jsx
│   │   └── main.jsx
│   ├── .gitignore
│   ├── package.json
│   ├── vite.config.js
│   └── README.md
├── backend/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/
│   │   │   │       └── goalglo/
│   │   │   │           └── backend/
│   │   │   │               ├── common/
│   │   │   │               │   ├── ErrorResponse.java
│   │   │   │               │   ├── GlobalExceptionHandler.java
│   │   │   │               │   ├── LoggingAspect.java
│   │   │   │               │   ├── ResourceNotFoundException.java
│   │   │   │               │   └── TokenCommons.java
│   │   │   │               ├── config/
│   │   │   │               │   ├── AwsSesConfig.java
│   │   │   │               │   ├── CacheConfig.java
│   │   │   │               │   ├── JwtConfig.java
│   │   │   │               │   ├── SecretConfig.java
│   │   │   │               │   ├── SecurityConfig.java
│   │   │   │               │   └── SecurityRateLimitFilter.java
│   │   │   │               ├── controllers/
│   │   │   │               │   ├── AdminActionController.java
│   │   │   │               │   ├── AppointmentController.java
│   │   │   │               │   ├── BlogPostController.java
│   │   │   │               │   ├── ContactMessageController.java
│   │   │   │               │   ├── HomeController.java
│   │   │   │               │   ├── InvoiceController.java
│   │   │   │               │   ├── PasswordResetController.java
│   │   │   │               │   ├── PaymentController.java
│   │   │   │               │   ├── PolicyController.java
│   │   │   │               │   ├── ServiceController.java
│   │   │   │               │   └── UserController.java
│   │   │   │               ├── dto/
│   │   │   │               │   ├── AdminActionDTO.java
│   │   │   │               │   ├── AppointmentDTO.java
│   │   │   │               │   ├── BlogPostDTO.java
│   │   │   │               │   ├── BulkImportDTO.java
│   │   │   │               │   ├── ContactMessageDTO.java
│   │   │   │               │   ├── EmailTemplateDTO.java
│   │   │   │               │   ├── InvoiceDTO.java
│   │   │   │               │   ├── PaymentDTO.java
│   │   │   │               │   ├── PolicyDTO.java
│   │   │   │               │   ├── RoleDTO.java
│   │   │   │               │   ├── ServiceDTO.java
│   │   │   │               │   ├── TimeSlotDTO.java
│   │   │   │               │   └── UserDTO.java
│   │   │   │               ├── entities/
│   │   │   │               │   ├── AdminAction.java
│   │   │   │               │   ├── Appointment.java
│   │   │   │               │   ├── BlogPost.java
│   │   │   │               │   ├── ContactMessage.java
│   │   │   │               │   ├── EmailTemplate.java
│   │   │   │               │   ├── EmailVerificationToken.java
│   │   │   │               │   ├── InvoiceEntity.java
│   │   │   │               │   ├── Payment.java
│   │   │   │               │   ├── Policy.java
│   │   │   │               │   ├── Role.java
│   │   │   │               │   ├── ServiceEntity.java
│   │   │   │               │   ├── TimeSlot.java
│   │   │   │               │   ├── Transaction.java
│   │   │   │               │   └── User.java
│   │   │   │               ├── hook/
│   │   │   │               │   └── StripeWebhook.java
│   │   │   │               ├── repositories/
│   │   │   │               │   ├── AdminActionRepository.java
│   │   │   │               │   ├── AppointmentRepository.java
│   │   │   │               │   ├── BlogPostRepository.java
│   │   │   │               │   ├── ContactMessageRepository.java
│   │   │   │               │   ├── EmailTemplateRepository.java
│   │   │   │               │   ├── EmailVerificationTokenRepository.java
│   │   │   │               │   ├── InvoiceRepository.java
│   │   │   │               │   ├── PaymentRepository.java
│   │   │   │               │   ├── PolicyRepository.java
│   │   │   │               │   ├── RoleRepository.java
│   │   │   │               │   ├── ServiceRepository.java
│   │   │   │               │   ├── TimeSlotRepository.java
│   │   │   │               │   └── UserRepository.java
│   │   │   │               ├── services/
│   │   │   │               │   ├── AdminActionService.java
│   │   │   │               │   ├── AppointmentService.java
│   │   │   │               │   ├── AwsSesEmailService.java
│   │   │   │               │   ├── BlogPostService.java
│   │   │   │               │   ├── ContactMessageService.java
│   │   │   │               │   ├── EmailTemplateService.java
│   │   │   │               │   ├── InvoiceService.java
│   │   │   │               │   ├── PasswordResetService.java
│   │   │   │               │   ├── PaymentService.java
│   │   │   │               │   ├── PolicyService.java
│   │   │   │               │   ├── ServiceService.java
│   │   │   │               │   ├── TimeSlotService.java
│   │   │   │               │   ├── UserService.java
│   │   │   │               │   └── UuidTokenService.java
│   │   │   │               ├── tokens/
│   │   │   │               │   └── JwtTokenUtil.java
│   │   │   │               └── BackendApplication.java
│   │   ├── resources/
│   │   ├── test/
│   ├── .mvn/
│   ├── target/
│   ├── backend.iml
│   ├── Dockerfile.backend
│   ├── mvnw
│   ├── mvnw.cmd
│   ├── pom.xml
│   └── README.md
├── schemas_designs/
│   ├── database_schema.sql
│   └── wireframes/
├── .gitignore
├── LICENSE.md
├── CONTRIBUTING.md
└── Jenkinsfile

```

### Contributing
Please read [CONTRIBUTING.md](CONTRIBUTING.md) for details on the code of conduct and the process for submitting pull requests.

### License
This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details.

### Contact
For further information, please contact:
- Name: Remy
- Email: remy@goalglo.com
- GitHub: https://github.com/ninganzaremy