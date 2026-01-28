package airesumescreener.airs.AirsApplication.InfrastructureDB;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;
import org.apache.tika.metadata.Metadata;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
public class DocumentParser {
    public String extractFromInputStream(InputStream InSt ) throws Exception{
        AutoDetectParser ADP = new AutoDetectParser();
        BodyContentHandler BCH = new BodyContentHandler(-1);
        Metadata metadata = new Metadata();
        try {
            ADP.parse(InSt, BCH, metadata);
            return BCH.toString();

        } catch(Exception e ) {
throw e ;
        }
    }

}
