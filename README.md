
# GoalGlo Application

## Introduction
GoalGlo is a comprehensive full-stack application designed to assist clients in learning about and planning their future investments and personal finance. 
## Features
- User Authentication and Authorization
- Blog System with CRUD operations
- Email Collection and Newsletter Integration
- Appointment Scheduling
- User Dashboard
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
- AWS (RDS, S3, Elastic Beanstalk, CloudFront)

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
│ ├── public/
│ ├── src/
│ │ ├── components/
│ │ ├── pages/
│ │ ├── redux/
│ │ ├── App.jsx
│ │ ├── index.jsx
│ │ └── ...
│ ├── package.json
│ └── ...
├── backend/
│ ├── src/
│ │ ├── main/
│ │ │ ├── java/
│ │ │ └── resources/
│ │ ├── test/
│ │ └── ...
│ ├── mvnw
│ ├── mvnw.cmd
│ ├── pom.xml
│ └── ...
├── README.md
└── ...
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