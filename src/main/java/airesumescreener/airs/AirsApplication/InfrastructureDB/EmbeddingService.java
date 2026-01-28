package airesumescreener.airs.AirsApplication.InfrastructureDB;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.TranslateException;
import org.springframework.stereotype.Service;
import java.util.Arrays;

@Service
public class EmbeddingService {

private final ZooModel<String , float[]> model;


    public EmbeddingService() throws Exception{


        Criteria<String , float[]> criteria = Criteria.builder().
                setTypes(String.class, float[].class).optModelUrls("djl://ai.djl.huggingface.pytorch/sentence-transformers/all-MiniLM-L6-v2")
                .optEngine("PyTorch")
                .optProgress(new ProgressBar())
                .build();

        this.model = criteria.loadModel();
    }


    public float[] returnEmbeds(String texts){
        try{


            var predictor = model.newPredictor();
            return predictor.predict(texts);
        }catch (TranslateException e) {
            throw new RuntimeException(e);
        }
    }


}
