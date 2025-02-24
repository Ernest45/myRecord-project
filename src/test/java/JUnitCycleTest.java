import org.junit.jupiter.api.*;

public class JUnitCycleTest {

    @BeforeAll
    //전체 테스트 시작 전 1회 실행하므로 static으로 선언
    static void beforeAll() {
        System.out.println("@BeforeAll");
        //전체 테스트 시작하기 전 처음 한 번만 실행
        //예를 들어 데이터베이스 연결 및 테스트 환경 초기화
        // 이 애너테이션은 실행 주기에 한번만 호출 되어야 하기에 static

    }

    @BeforeEach
    //테스트 실행 전에 매번 호출 객체 초기화 및 값 세팅
    // 각 인스턴스에 대해 메서드를 호출 해야하기에 static으로 하면 안됨
    public void beforeEach() {
        System.out.println("@BeforeEach");
    }



    @Test
    public void test1() {

        System.out.println("test1");

    }

    @Test
    public void test2() {
        System.out.println("test2");

    }

    @Test
    public void test3() {
        System.out.println("test3");

    }

    @AfterAll
    // 전체 테스트 마치고 종료 전 한번 연결, 데이터베이스 연결 해제 및 공통 상용 자원 해제때
    // 마찬가지로 한번만 호출하기에 static
    static void afterAll() {
        System.out.println("@AfterAll");
    }

    @AfterEach
    // 각 테스트 종료 전 매번 실행
    // 특정 데이터를 삭제 해야하기에static x
    public void afterEach() {
        System.out.println("@AfterEach");

    }
}
