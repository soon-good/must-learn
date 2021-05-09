package singleton.moving_points;

import io.study.design_pattern.singleton.moving_points.Offset;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class OffsetTest {

	@Test
	void 상하좌우_인스턴스테스트(){
		Offset top = Offset.top();
		System.out.println("top = " + top);
		Assertions.assertSame(top, Offset.top());

		Offset bottom = Offset.bottom();
		System.out.println("bottom = " + bottom);
		Assertions.assertSame(bottom, Offset.bottom());

		Offset left = Offset.left();
		System.out.println("left = " + left);
		Assertions.assertSame(left, Offset.left());

		Offset right = Offset.right();
		System.out.println("right = " + right);
		Assertions.assertSame(right, Offset.right());

	}
}
