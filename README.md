An intelligent resume generator built using Spring Boot (backend) and React + Vite (frontend) that creates professional resumes based on a user-provided prompt. The system also includes features like PDF export, and resume editor.
## Steps to Run the AI Resume Builder Project  

#### **Prerequisites:**  
- Install **Ollama** on your computer.  
- Download the **'deepseek-r1-7b'** model (4GB file).  

#### **Setup & Execution:**  

1. **Run Backend (Spring Boot):**  
   - Start the Spring Boot backend.  
   - It will run on `localhost:8080` (or another port).  
   - If the port is different, update it in the frontend file `ResumeService.js`.  

2. **Setup Frontend (React):**  
   - Navigate to the frontend directory.  
   - Run `npm install` to install dependencies.  

3. **Start Frontend:**  
   - Run `npm run dev`.  
   - Open the localhost URL where React is running.  

4. **Using the Application:**  
   - Click the **"Get Started"** button.  
   - Enter a **prompt** with details for your resume.  
   - Click **"Submit"** to generate your resume.  
   - Edit the resume if needed.  
   - Click **"Print"** to save it as a PDF.  

Now your AI-powered resume is ready! 🚀
