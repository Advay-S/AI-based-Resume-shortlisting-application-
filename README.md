# AI-Based Resume Shortlisting Application

An intelligent resume screening system that uses AI-powered vector embeddings and semantic search to match candidate resumes with job descriptions. Built with Spring Boot and PostgreSQL with pgvector extension.

## 🌟 Key Features

- **AI-Powered Resume Parsing**: Automatically extracts text from PDF resumes using Apache Tika
- **Vector Embeddings**: Generates semantic embeddings using the `all-MiniLM-L6-v2` sentence transformer model
- **Semantic Search**: Finds the best matching candidates using cosine similarity on vector embeddings
- **Modern Web Interface**: 
  - Candidate upload page with drag-and-drop functionality
  - HR dashboard for job description matching
  - Real-time match scoring and ranking
- **Database-Backed**: Uses PostgreSQL with pgvector extension for efficient vector similarity search
- **REST API**: Clean RESTful endpoints for resume upload and candidate matching

## 🛠️ Technology Stack

### Backend
- **Java 21**
- **Spring Boot 3.4.1**
  - Spring Web
  - Spring Data JPA
  - Spring Boot DevTools
- **Maven** - Dependency management and build tool

### AI/ML Libraries
- **DJL (Deep Java Library) 0.25.0**
  - PyTorch engine
  - HuggingFace tokenizers
  - Sentence transformer model: `all-MiniLM-L6-v2`

### Document Processing
- **Apache Tika 2.9.1** - PDF parsing and text extraction
- **Apache OpenNLP 1.9.2** - Natural language processing

### Database
- **PostgreSQL** with **pgvector** extension - Vector similarity search
- **Hypersistence Utils 3.9.0** - PostgreSQL vector type support

### Additional Libraries
- **Lombok** - Reduce boilerplate code
- **Commons IO** - File handling utilities
- **MinIO** - Object storage (configured for future use)

### Frontend
- **HTML5/CSS3/JavaScript**
- **Tailwind CSS** - Modern styling
- **Font Awesome** - Icons

## 📋 Prerequisites

Before you begin, ensure you have the following installed:

- **Java Development Kit (JDK) 21** or higher
- **Maven 3.6+** (or use the included Maven wrapper)
- **PostgreSQL 12+** with **pgvector** extension
- **Git** (for cloning the repository)

## 🚀 Installation and Setup

### 1. Clone the Repository

```bash
git clone https://github.com/Advay-S/AI-based-Resume-shortlisting-application-.git
cd AI-based-Resume-shortlisting-application-
```

### 2. Set Up PostgreSQL with pgvector

#### Install PostgreSQL
```bash
# Ubuntu/Debian
sudo apt-get update
sudo apt-get install postgresql postgresql-contrib

# macOS (using Homebrew)
brew install postgresql
```

#### Install pgvector Extension
```bash
# Ubuntu/Debian
sudo apt-get install postgresql-server-dev-all
cd /tmp
git clone https://github.com/pgvector/pgvector.git
cd pgvector
make
sudo make install

# macOS (using Homebrew)
brew install pgvector
```

#### Create Database and Enable Extension
```bash
# Connect to PostgreSQL
sudo -u postgres psql

# Create database
CREATE DATABASE resume_db;

# Connect to the database
\c resume_db

# Enable pgvector extension
CREATE EXTENSION IF NOT EXISTS vector;

# Create the table (or use the init.sql file)
CREATE TABLE IF NOT EXISTS candidate_profiles (
    id SERIAL PRIMARY KEY,
    full_text TEXT,
    embedding vector(384)
);
```

Alternatively, you can use the provided `init.sql` file:
```bash
sudo -u postgres psql -d resume_db -f init.sql
```

### 3. Configure Database Connection

Create or update `src/main/resources/application.properties`:

```properties
# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/resume_db
spring.datasource.username=postgres
spring.datasource.password=your_password_here
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# File Upload Configuration (adjust as needed)
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
```

### 4. Build the Project

Using Maven wrapper (recommended):
```bash
./mvnw clean install
```

Or using your system Maven:
```bash
mvn clean install
```

### 5. Run the Application

```bash
./mvnw spring-boot:run
```

Or run the generated JAR:
```bash
java -jar target/AirsApplication-0.0.1-SNAPSHOT.jar
```

The application will start on `http://localhost:8080`

## 🖥️ Using the Application

### Candidate Resume Upload

1. Navigate to `http://localhost:8080/ClientSidepage.html`
2. Drag and drop a PDF resume or click to browse
3. The system will:
   - Extract text from the PDF
   - Generate vector embeddings
   - Store in the database

### HR Dashboard - Find Matching Candidates

1. Navigate to `http://localhost:8080/HRSidePage.html`
2. Enter a job description in the text area
3. Click "Run Analysis"
4. View ranked candidates with match scores
5. Candidates are sorted by semantic similarity (0-100%)

## 📡 API Endpoints

### Upload Resume
```http
POST /api/resumecontroller/upload
Content-Type: multipart/form-data

Parameters:
  file: PDF file (required)

Response: String confirmation message
```

### Match Job Description
```http
POST /api/resumecontroller/match
Content-Type: text/plain

Body: Job description text

Response: JSON array of matching resumes with scores
```

### Rank Match (Preferred)
```http
POST /api/resumecontroller/rankmatch
Content-Type: text/plain

Body: Job description text

Response: JSON array of ranked resumes with match scores
Format: "Match Score: 0.XX | [resume text]"
```

## 📁 Project Structure

```
AI-based-Resume-shortlisting-application-/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── airesumescreener/airs/AirsApplication/
│   │   │       ├── AirsApplication.java          # Main Spring Boot application
│   │   │       └── InfrastructureDB/
│   │   │           ├── DocumentParser.java       # PDF text extraction
│   │   │           ├── EmbeddingService.java     # AI vector generation
│   │   │           ├── ResumeController.java     # REST API endpoints
│   │   │           ├── ResumeRepository.java     # Business logic
│   │   │           └── analyse.java              # Analysis utilities
│   │   └── resources/
│   │       ├── static/
│   │       │   ├── ClientSidepage.html          # Candidate upload UI
│   │       │   └── HRSidePage.html              # HR matching UI
│   │       └── application.properties            # Configuration
│   └── test/
│       └── java/
│           └── airesumescreener/airs/AirsApplication/
│               └── AirsApplicationTests.java     # Unit tests
├── pom.xml                                       # Maven dependencies
├── init.sql                                      # Database initialization
├── mvnw                                          # Maven wrapper (Unix)
├── mvnw.cmd                                      # Maven wrapper (Windows)
└── README.md                                     # This file
```

## 🔬 How It Works

### 1. Resume Upload Process
1. User uploads a PDF resume via the web interface
2. `DocumentParser` extracts text using Apache Tika
3. `EmbeddingService` generates a 384-dimensional vector embedding using the pre-trained sentence transformer model
4. Resume text and embedding are stored in PostgreSQL with pgvector

### 2. Candidate Matching Process
1. HR user enters a job description
2. System generates vector embedding for the job description
3. PostgreSQL performs cosine similarity search using pgvector's `<=>` operator
4. Results are ranked by similarity score (1 - distance)
5. Top 20 matches are returned with scores

### 3. Vector Similarity Formula
```sql
SELECT 
    1 - (embedding <=> job_vector) as score, 
    full_text 
FROM candidate_profiles 
ORDER BY score DESC 
LIMIT 20
```

The `<=>` operator calculates cosine distance, and `1 - distance` gives the similarity score.

## 🧪 Testing

Run the test suite:
```bash
./mvnw test
```

## 🔧 Configuration Options

### Adjust Model (in EmbeddingService.java)
The default model is `all-MiniLM-L6-v2`. To use a different model, modify the `Criteria` builder in the `EmbeddingService` constructor:
```java
Criteria<String, float[]> criteria = Criteria.builder()
    .setTypes(String.class, float[].class)
    .optModelUrls("djl://ai.djl.huggingface.pytorch/sentence-transformers/your-model-name")
    .optEngine("PyTorch")
    .optProgress(new ProgressBar())
    .build();
```

### Adjust Result Limit
Modify the `LIMIT` in the SQL query in `ResumeRepository.java` (line 76-83):
```java
String sql = """
    SELECT 
        1 - (embedding <=> ?::vector) as score, 
        full_text 
    FROM candidate_profiles 
    ORDER BY score DESC 
    LIMIT 20  -- Change this value to show more/fewer results
""";
```

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📝 License

This project is open source and available under the [MIT License](LICENSE).

## 🐛 Troubleshooting

### Issue: Model Download Fails
**Solution**: Ensure you have a stable internet connection. The DJL library downloads the model on first run (~80MB).

### Issue: pgvector Extension Not Found
**Solution**: Make sure pgvector is properly installed and the extension is enabled in your database:
```sql
CREATE EXTENSION IF NOT EXISTS vector;
```

### Issue: Out of Memory Error
**Solution**: Increase JVM heap size:
```bash
java -Xmx2g -jar target/AirsApplication-0.0.1-SNAPSHOT.jar
```

### Issue: PDF Parsing Fails
**Solution**: Ensure the PDF is not corrupted and contains extractable text (not scanned images).

## 📞 Support

For issues, questions, or suggestions, please open an issue on the [GitHub repository](https://github.com/Advay-S/AI-based-Resume-shortlisting-application-/issues).

## 🎯 Future Enhancements

- [ ] Support for multiple file formats (DOCX, TXT, etc.)
- [ ] Batch resume upload
- [ ] Advanced filtering (experience, skills, location)
- [ ] Resume ranking explanations
- [ ] Export results to CSV/Excel
- [ ] User authentication and authorization
- [ ] Resume deduplication
- [ ] Analytics dashboard
- [ ] Integration with ATS (Applicant Tracking Systems)

---

**Built with ❤️ using Spring Boot, DJL, and PostgreSQL pgvector**
