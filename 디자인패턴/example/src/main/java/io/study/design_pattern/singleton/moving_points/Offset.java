package io.study.design_pattern.singleton.moving_points;

public class Offset{
	private final int row;
	private final int col;

	private Offset(int row, int col){
		this.row = row;
		this.col = col;
	}

	public static Offset top() { return InnerOffset.MOVE_TOP; }
	public static Offset bottom() { return InnerOffset.MOVE_BOTTOM; }
	public static Offset left() { return InnerOffset.MOVE_LEFT; }
	public static Offset right() { return InnerOffset.MOVE_RIGHT; }

	private static class InnerOffset {
		private static final Offset MOVE_TOP 	= new Offset(-1, 0);
		private static final Offset MOVE_BOTTOM = new Offset(1, 0);
		private static final Offset MOVE_LEFT 	= new Offset(0, -1);
		private static final Offset MOVE_RIGHT 	= new Offset(0, 1);
	}

	@Override
	public String toString() {
		return "Offset {" + "row = " + row + ", col = " + col + " }";
	}
}
