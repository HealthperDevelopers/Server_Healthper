package umc.healthper;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

@Slf4j
public class SectionTest {

    @Test
    @DisplayName("동시성 문제 확인")
    void conflict(){

        Runnable userA = () ->{
            log.info("userA section start");
            List<Section> sections = Section.strToSection("0000000001");
            sleep(2000);
            log.info("userA section done {}", sections);
        };
        Runnable userB = () ->{
            log.info("userB section start");
            List<Section> sections = Section.strToSection("1111111110");
            log.info("userB section done {}", sections);
        };

        Thread threadA = new Thread(userA);
        threadA.setName("thread-A");
        Thread threadB = new Thread(userB);
        threadB.setName("thread-B");

        threadA.start();
        threadB.start();
        sleep(3000);
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
