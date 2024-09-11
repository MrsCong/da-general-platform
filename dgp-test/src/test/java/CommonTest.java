import com.dgp.test.DgpTestApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

@SpringBootTest(classes = {DgpTestApplication.class})
public class CommonTest {

    private static final ExpressionParser PARSER = new SpelExpressionParser();
    @Test
    void test1() {
        com.dgp.test.entity.Test test = new com.dgp.test.entity.Test();
        test.setName("xxx");
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setVariable("test",test);
        Object value = PARSER.parseExpression("#test.name").getValue(context);
        System.out.println(value.toString());
    }

}
