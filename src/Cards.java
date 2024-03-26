public class Cards {
    private final String _color;
    private final String _figure;
    private final int _valueOfFigure;
    private final int _valueOfColor;
    public int getValueOfFigure() {
        return _valueOfFigure;
    }
    public int getValueOfColor() {
        return _valueOfColor;
    }
    public Cards(String color, String figure, int valueF, int valueC) {
        this._color = color;
        this._figure = figure;
        this._valueOfFigure = valueF;
        this._valueOfColor = valueC;
    }
    public int compareFig(Cards o){
        return Integer.compare(_valueOfFigure, o._valueOfFigure);
    }
    public int compareCol(Cards o){
        return Integer.compare(_valueOfColor, o._valueOfColor);
    }
    public int compare(Cards o) {
        if (_valueOfFigure == o._valueOfFigure) {
            return Integer.compare(_valueOfColor, o._valueOfColor);
        } else if (_valueOfFigure > o._valueOfFigure) {
            return 1;
        } else {
            return -1;
        }
    }

    @Override
    public String toString() {
        return _figure + _color;
    }
}