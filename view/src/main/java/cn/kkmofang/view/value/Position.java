package cn.kkmofang.view.value;

/**
 * Created by hailong11 on 2018/1/17.
 */

public enum Position {
    None,Top,Bottom,Left,Right;

    public static Position valueOf(Object v) {

        if("top".equals(v)) {
            return Top;
        }

        if("bottom".equals(v)) {
            return Bottom;
        }

        if("left".equals(v)) {
            return Left;
        }

        if("right".equals(v)) {
            return Right;
        }

        return None;
    }
}
