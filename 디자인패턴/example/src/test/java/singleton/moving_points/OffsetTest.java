package singleton.moving_points;

import io.study.design_pattern.singleton.moving_points.Offset;
import org.junit.jupiter.api.Test;

public class OffsetTest {

	@Test
	void 상하좌우_인스턴스테스트(){
		Offset top = Offset.top();
		System.out.println("top = " + top);

		Offset bottom = Offset.bottom();
		System.out.println("bottom = " + bottom);

		Offset left = Offset.left();
		System.out.println("left = " + left);

		Offset right = Offset.right();
		System.out.println("right = " + right);

	}
}
