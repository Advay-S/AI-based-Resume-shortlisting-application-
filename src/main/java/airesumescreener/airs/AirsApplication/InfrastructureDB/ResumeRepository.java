package airesumescreener.airs.AirsApplication.InfrastructureDB;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import java.util.Arrays;
import java.util.*;

import java.io.IOException;
import java.io.InputStream;

//This class processes resumes ...that is it takes in resumes and calls the document parser class

@Service
public class ResumeRepository{

    private final DocumentParser documentParser ;
    private final EmbeddingService embeddingService;
    private final JdbcTemplate jdbctemplate;


    @Autowired
    public ResumeRepository(DocumentParser documentParser, EmbeddingService embeddingService, JdbcTemplate jdbctemplate) {
        this.documentParser = documentParser;
        this.embeddingService = embeddingService;
        this.jdbctemplate = jdbctemplate;
    }



public void processResume( @RequestParam("file") MultipartFile file){
    System.out.println("--- CHECKPOINT 1: Service started ---");

    try{
        System.out.println("--- CHECKPOINT 2: Getting Stream ---");
        InputStream inputStream = file.getInputStream();

        System.out.println("--- CHECKPOINT 3: Calling Parser ---");
        String rawText = documentParser.extractFromInputStream(inputStream);

        float[] vectors = embeddingService.returnEmbeds(rawText);

        String vecString = Arrays.toString(vectors);

        System.out.println("--- CHECKPOINT 4: Inserting into db! ---");

        String sql = "INSERT INTO candidate_profiles (full_text, embedding) VALUES (?, ?::vector)";

        jdbctemplate.update(sql, rawText, vecString );

        System.out.println("--- SUCCESS: Saved to Postgres! ---");



        System.out.println("--- CHECKPOINT 5: Success! ---");
        System.out.println("Here are the vectors : " + Arrays.toString(Arrays.copyOfRange(vectors, 0, 5)));
        System.out.println("Extracted text : " + rawText );
    } catch (IOException e) {
        throw new RuntimeException(e);
    } catch (Exception e) {
        System.out.println("--- CRASH REPORT ---");
        System.out.println("It crashed because: " + e.getMessage());
        System.out.println("Error Class: " + e.getClass().getName());
        e.printStackTrace(); // This forces the red text to appear
        throw new RuntimeException(e);
    }
}


        public List<String> findMatchingResume(String jobDesc){
        float[] jdVector = embeddingService.returnEmbeds(jobDesc);

        String jdvstring = Arrays.toString(jdVector);
            String sql = """
        SELECT 
            1 - (embedding <=> ?::vector) as score, 
            full_text 
        FROM candidate_profiles 
        ORDER BY score DESC 
        LIMIT 20
    """;

            return jdbctemplate.query(sql, (rs, rowNum) -> {
                double score = rs.getDouble("score");
                String text = rs.getString("full_text");
                // Returns: "Score: 0.95 | Advay Shinde..."
                return "Match Score: " + String.format("%.2f", score) + " | " + text;
            }, jdvstring);
}


}


