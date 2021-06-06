package objects;

import gamelogic.LogicController;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class Car {

    double reactiontime = (Math.random() * ((1.3 - 0.8) + 1)) + 0.8;
    double breakingfactor = Math.random() * ((1-0.5)+1) + 0.5;
    double acceleration = Math.random() * ((1-0.5)+1) + 0.5;
    final int[] xa  = new int[] {-40,40,40,-40};
    final int[] ya  = new int[] {-40,-40,40,40};
    Polygon poly1 = new Polygon(xa, ya, 4);
    int x = 0;
    int y = 0;
    private boolean dxp = false;
    private boolean dyp = false;

    ArrayList<Object[]> path;
    Street currentstreet;
    int streetindex = 0;
    public int lane = 0;
    int dir = 0;
    Lane[] lanes;
    int nodeinstreet = 0;


    private Map<String,Point> cords = (Map<String,Point>) LogicController.connections[2];

    MyObject object;
    public Car(String origin,String destination){
        path = gamelogic.Pathfinding.find_path(origin,destination);

        currentstreet = (Street)path.get(streetindex)[2];
        streetindex++;
        if((int)(path.get(streetindex)[3]) == 1){
            lanes = currentstreet.forward;
            dir = 1;
        }
        else{
            lanes = currentstreet.backward;
            dir = -1;
        }
        lane = 0;
        lanes[lane].cars.add(this);

        nodeinstreet = Arrays.asList(currentstreet.nodes).indexOf(cords.get(origin));

        x = cords.get(origin).x;
        y = cords.get(origin).y;

        object=  new MyObject(new Polygon[] {poly1},1,new Color(110, 255, 78));
        move(x,y);

        LogicController.cars.add(object);

    }

    public void move(int posx,int posy){
        x = posx;
        y = posy;
        int[] xp = xa.clone();
        int[] yp = ya.clone();
        for(int i= 0;i<xp.length;i++){
            xp[i]+= posx;

        }
        for(int i= 0;i<yp.length;i++){
            yp[i]+= posy;

        }
        object.polys = new Polygon[] {new Polygon(xp,yp,object.polys[0].npoints)};

    }

    public boolean update(){
        if(streetindex < path.size()+1){

            if(nodeinstreet<lanes[0].points.length && nodeinstreet > -1 && nodeinstreet != (int)(path.get(streetindex-1)[4])+dir){

                int dx = lanes[lane].points[nodeinstreet].x - x;
                int dy = lanes[lane].points[nodeinstreet].y - y;

                if((dx > 0 != dxp) | (dy >0 != dyp)  | (dx == 0) | (dy == 0) ){
                    move(lanes[lane].points[nodeinstreet].x, lanes[lane].points[nodeinstreet].y);
                    nodeinstreet += dir;
                    System.out.println(dx);
                    if(nodeinstreet<lanes[0].points.length && nodeinstreet > -1) {
                        dx = lanes[lane].points[nodeinstreet].x - x;
                        dy = lanes[lane].points[nodeinstreet].y - y;

                        if (dx > 0) {
                            dxp = true;
                        } else {
                            dxp = false;
                        }
                        if (dy > 0) {
                            dyp = true;
                        } else {
                            dyp = false;
                        }
                    }
                    update();
                }
                else {
                    double scale = 100 / Math.sqrt(dx * dx + dy * dy);


                    move((int) (x + dx * scale), (int) (y + dy * scale));
                }
                //move(lanes[lane].points[nodeinstreet].x,lanes[lane].points[nodeinstreet].y);


            }
            else{
                if(streetindex >= path.size()){return false;}

                currentstreet = (Street) path.get(streetindex)[2];
                lanes[lane].cars.remove(this);
                if((int)(path.get(streetindex)[3]) == 1){
                    lanes = currentstreet.forward;
                    dir = 1;
                }
                else{
                    lanes = currentstreet.backward;
                    dir = -1;
                }
                nodeinstreet = Arrays.asList(currentstreet.nodes).indexOf(cords.get(path.get(streetindex-1)[0]));
                streetindex++;
                lane = 0;
                lanes[lane].cars.add(this);

                int dx = lanes[lane].points[nodeinstreet].x - x;
                int dy = lanes[lane].points[nodeinstreet].y - y;

                if(dx >0) {dxp=true;}else{dxp=false;}
                if(dy >0) {dyp=true;}else{dyp=false;}

                update();

            }
        }
        else{

            //reached end of destination
            LogicController.cars.remove(object);
            object = null;
            return false;
        }
    return true;
    }

}
