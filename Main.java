import gamelogic.LogicController;
import ui.Display;
import objects.Car;

class Main {
    public static void main(String[] args) {
        LogicController.map("map.osm");
        Display.create();
        LogicController logic = new LogicController();



            LogicController.carai.add(new Car("8313271112", "5167797079"));

    }
}