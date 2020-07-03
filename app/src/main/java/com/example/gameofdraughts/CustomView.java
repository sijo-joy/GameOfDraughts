package com.example.gameofdraughts;

//imports

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;


import java.util.ArrayList;

public class CustomView extends View {

    private Paint square1Color, player2Color, player1Color, square2Colour;
    public int viewWidth;
    public float individualSquareBoxSize;
    private boolean touches[];
    private float touchx[];
    private float touchy[];
    private boolean touching;
    private int first;
    Rect square;
    Paint highlightPaint;
    Paint kinged1;
    Paint kinged2;
    ArrayList<Rect> squares;
    Rect touchRect;
    ArrayList<CircleCentre> circleCordinates;
    CircleCentre center;
    CircleCentre touchCircle;
    float a = 0, b=0;
    boolean isHighlighted = false;
    int count = 0;
    String oldCircleCenter , newCircleCenter, capturedCircleCenter;
    boolean alreadyDrawnFirst;
    int currentPlayer, greenCount, redCount;

    TextView p1TV , p2TV, currentTV;


    float u ;
    float v ;
    float nu ;
    float nv ;
    float cu ;
    float cv ;


    public CustomView(Context c) {
        super(c);
        init();
    }

    public CustomView(Context c, AttributeSet as) {
        super(c, as);
        init();
    }

    public CustomView(Context c, AttributeSet as, int default_style) {
        super(c, as, default_style);
        init();
    }

    //refactored init method as most of this code is shared by all the constructores
    public void init() {
        //create the paint object for rendering the squares
        square1Color = new Paint(Paint.ANTI_ALIAS_FLAG);
        player2Color = new Paint(Paint.ANTI_ALIAS_FLAG);
        player1Color = new Paint(Paint.ANTI_ALIAS_FLAG);
        square2Colour = new Paint(Paint.ANTI_ALIAS_FLAG);
        square1Color.setColor(0xFF0000FF);
        player2Color.setColor(0xFF00FF00);
        player1Color.setColor(0xFFFF0000);
        square2Colour.setColor(0xFFFFFF00);
        kinged1 = new Paint(Paint.ANTI_ALIAS_FLAG);
        kinged2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        kinged1.setColor(0xFFFF00FF);
        kinged2.setColor(0xFF00FFFF);
        touches = new boolean[16];
        touchx = new float[16];
        touchy = new float[16];
        currentPlayer = 0;
        float direction;

        redCount =12;
        greenCount = 12;
        alreadyDrawnFirst = false;
        oldCircleCenter = "";
        newCircleCenter = " ";


//       p1TV = findViewById(R.id.p1CountTV);
//       p2TV = findViewById(R.id.p2CountTV);
//       currentTV = (TextView) findViewById(R.id.currentPlayerTV);

        squares = new ArrayList<>();

        //list containing the cordinates of the each circle
        circleCordinates = new ArrayList<>();
       // center = new CircleCentre();

        //start off with nothing touching the view
        touching = false;


        //initialise the rectangle
        // square = new Rect(-100,-100,100,100);

        highlightPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        highlightPaint.setColor(0xff444444);
        touchCircle = new CircleCentre();

//        reset.setOnClickListener(new View.OnClickListener()  {
//            @Override
//            public void onClick(View v) {
//               // drawPieces(Canvas c);
//
//            }
//        });
    }

    //method to draw the content
    public void onDraw(Canvas canvas) {
        //call the superclass method
        super.onDraw(canvas);
        drawGameBoard(canvas);
        RedrawCircles(canvas);
//        SetCanvas(canvas);


        if (touchRect != null ) {
            drawHighlightSquare(canvas);
        }


    }

//    public void SetCanvas(Canvas canvas)    {
//        this.c = canvas;
//    }

    private void drawGameBoard(Canvas canvas) {
        p1TV.setText("Player 1's count: " + redCount);
        p2TV.setText("Player 2's count: " + greenCount);

        int left = 0;
        int top = 0;
        int right = (int) individualSquareBoxSize;
        int bottom = (int) individualSquareBoxSize;
        Rect rect;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {


                if (i % 2 == 0) {

                    Paint paint = (j % 2 == 0) ? square2Colour : square1Color;
                    rect = new Rect(left, top, right, bottom);
                    canvas.drawRect(rect, paint);

                } else {
                    Paint paint = (j % 2 == 0) ? square1Color : square2Colour;
                    rect = new Rect(left, top, right, bottom);
                    canvas.drawRect(rect, paint);

                }
                if (squares.size() < 64) {
                    squares.add(rect);

                }

                right += individualSquareBoxSize;
                left += individualSquareBoxSize;
            }

            left = 0;
            top += (int)individualSquareBoxSize;
            right = (int) individualSquareBoxSize;
            bottom += (int) individualSquareBoxSize;

        }

    }

    public void drawPieces(Canvas canvas) {
        float x = ((3 * individualSquareBoxSize) / 2);
        float y = individualSquareBoxSize / 2;
        for (int j = 0; j < 8; j++) {
            for (int i = 0; i < 4; i++) {
                if (j == 3 || j == 4) {
                    continue;
                }
                Paint paint = (j > 4) ? player2Color : player1Color;
                canvas.drawCircle(x, y, individualSquareBoxSize / 3, paint);

                if(circleCordinates.size()<24) {
                    center = new CircleCentre();
                    center.setX(x);
                    center.setY(y);
                    center.setPaint(paint);
                    if(j<=3) {
                        center.setId(1);
                    }
                    else {
                        center.setId(2);
                    }
                        circleCordinates.add(center);
                }
                x += 2 * individualSquareBoxSize;
            }

            if (j % 2 == 0) {
                x = individualSquareBoxSize / 2;
            } else {
                x = ((3 * individualSquareBoxSize) / 2);
            }
            y += individualSquareBoxSize;
        }

    }

    private void drawHighlightSquare(Canvas canvas) {
        highlightPaint.setAlpha(170);
        float x = touchRect.exactCenterX();
        float y = touchRect.exactCenterY();
        //  boolean f = compareCircle(x,y);

        if(compareCircle(x,y)) {
            int c = count;
//            invalidate();

            a = x;
            b = y;

            oldCircleCenter = a + "|" + b;
            if(newCircleCenter == null) {
                canvas.drawRect(touchRect, highlightPaint);
            }
            newCircleCenter = null;

        }
        else
        {
            newCircleCenter = x + "|" + y;
        }


    }

    public CircleCentre getCapturedSquare (float x, float y){
        CircleCentre r = null;
        for(CircleCentre c : circleCordinates){
            if (c.getX() == x && c.getY() == y){
                r= c;
            }
        }
        return r;
    }

    public void simpleMovePlayer1(CircleCentre c, float x, float y,float nx, float ny) {

        if (nx == x + individualSquareBoxSize || nx == x - individualSquareBoxSize) {
                    c.setX(nx);
                    c.setY(ny);
                    if (isKinged(c)) {
                        c.setId(3);
                        c.setPaint(kinged1);
                    }
                    currentPlayer = 1;
                    currentTV.setText("Next turn: Player 2");
        }
    }

    public void simpleMovePlayer2(CircleCentre c, float x, float y,float nx, float ny) {

                    if (nx== x + individualSquareBoxSize || nx == x - individualSquareBoxSize) {
                        c.setX(nu);
                        c.setY(nv);
                        if (isKinged(c)) {
                            c.setId(4);
                            c.setPaint(kinged2);
                        }
                        currentPlayer = 2;
                        currentTV.setText("Next turn: Player 1");


                    }
    }



    public void captureMovePlayer1(CircleCentre c, float x,float y,float nx, float ny, float cx,float cy) {

                if (nx == x + (2 * individualSquareBoxSize) || nx == x - (2 * individualSquareBoxSize)) {
                    CircleCentre captured = getCapturedSquare(cx, cy);
                    if(captured.getId()==2 || captured.getId() ==4) {
                        c.setX(nx);
                        c.setY(ny);
                        if (isKinged(c)) {
                            c.setId(3);
                            c.setPaint(kinged1);
                        }
                        circleCordinates.remove(captured);
                        greenCount--;
                       p2TV.setText("Player 2's count: "+greenCount);
                        currentPlayer = 1;
                        currentTV.setText("Next turn: Player 2");
                    }

                    float l = c.getX() + (2 * individualSquareBoxSize);
                    float m = c.getX() - (2 * individualSquareBoxSize);
                    float n = c.getY() + (2 * individualSquareBoxSize);
                    float nN =c.getY() - (2 * individualSquareBoxSize);
                    float o = c.getX() + (individualSquareBoxSize);
                    float p = c.getX() - (individualSquareBoxSize);
                    float q = c.getY() + (individualSquareBoxSize);
                    float qN = c.getY() - (individualSquareBoxSize);


                    CircleCentre circleLeft;
                    CircleCentre circleRight;
                    int idR = 0;
                    int idL = 0;
                    int idNR = 0;
                    int idNL = 0;
                    if(getCapturedSquare(o,q) !=null){
                        circleRight = getCapturedSquare(o,q);
                        idR = circleRight.getId();
                    }
                    if(getCapturedSquare(o,qN) != null){
                        circleRight = getCapturedSquare(o,qN);
                        idNR = circleRight.getId();
                    }

                    if(getCapturedSquare(p,q) !=null){
                        circleLeft = getCapturedSquare(p,q);
                        idL = circleLeft.getId();
                    }
                    if(getCapturedSquare(p,qN) != null){
                        circleLeft = getCapturedSquare(p,qN);
                        idNL = circleLeft.getId();
                    }

                    if ((((!compareCircle(l, n)) && (compareCircle(o,q))) && ((idR==2)||(idR == 4)) && (getSquare((int)l,(int)n) !=null))
                            || ((!compareCircle(m, n)) && (compareCircle(p,q)) && ((idL==2)||(idL==4)) && getSquare((int)m,(int)n)!=null)){
                        currentPlayer = 2;
                        currentTV.setText("Next turn: Player 1");
                    }
                    if(c.getId() == 3){
                        if (((!compareCircle(l, nN)) && (compareCircle(o,qN)) && ((idNR ==2)||(idNR==4)) && (getSquare((int)l,(int)nN) !=null) )
                                || (((!compareCircle(m,nN)) && (compareCircle(p,qN))) && ((idNL ==2)||(idNL==4)) && (getSquare((int)m,(int)nN )!=null))){
                            currentPlayer = 2;
                            currentTV.setText("Next turn: Player 1");
                        }
                    }
                }
    }

    public void captureMovePlayer2(CircleCentre c, float x,float y,float nx, float ny, float cx,float cy) {

        if ((nx == x + (2 * individualSquareBoxSize) || nx == x - (2 * individualSquareBoxSize))) {
                    CircleCentre captured = getCapturedSquare(cx, cy);
                    if(captured.getId()==1 || captured.getId() ==3) {
                        c.setX(nx);
                        c.setY(ny);
                        if (isKinged(c)) {
                            c.setId(4);
                            c.setPaint(kinged2);
                        }
                        circleCordinates.remove(captured);
                        redCount--;
                        p1TV.setText("Player 1's count: "+redCount);
                        currentPlayer = 2;
                        currentTV.setText("Next turn: Player 1");
                    }
                    float l = c.getX() + (2*individualSquareBoxSize);
                    float m = c.getX() - (2*individualSquareBoxSize);
                    float n = c.getY() - (2*individualSquareBoxSize);
                    float nP = c.getY() + (2*individualSquareBoxSize);
                    float o = c.getX() + (individualSquareBoxSize);
                    float p = c.getX() - (individualSquareBoxSize);
                    float q = c.getY() - (individualSquareBoxSize);
                    float qP = c.getY() + (individualSquareBoxSize);
                    CircleCentre circleLeft;
                    CircleCentre circleRight;
                    int idR = 0;
                    int idL = 0;
                    int idPR =0;
                    int idPL = 0;
                    if(getCapturedSquare(o,q) !=null){
                        circleRight = getCapturedSquare(o,q);
                        idR = circleRight.getId();
                    }
                    if(getCapturedSquare(o,qP) != null){
                        circleRight = getCapturedSquare(o,qP);
                        idPR = circleRight.getId();
                    }

                    if(getCapturedSquare(p,q) !=null){
                         circleLeft = getCapturedSquare(p,q);
                       idL= circleLeft.getId();
                    }
                    if(getCapturedSquare(p,qP) !=null){
                        circleLeft = getCapturedSquare(p,qP);
                        idPL = circleLeft.getId();

                    }

                    if (((!compareCircle(l, n)) && (compareCircle(o,q)) && ((idR ==1)||(idR==3)) && (getSquare((int)l,(int)n) !=null) )
                            || (((!compareCircle(m, n)) && (compareCircle(p,q))) && ((idL ==1)||(idL==3))&& (getSquare((int)m,(int)n )!=null))){
                        currentPlayer = 1;
                       currentTV.setText("Next turn: Player 2");
                    }

                    if(c.getId()==4){
                        if ((((!compareCircle(l, nP)) && (compareCircle(o,qP))) && ((idPR== 1)||(idPR==3)) && (getSquare((int)l,(int)nP) !=null))
                                || ((!compareCircle(m, nP)) && (compareCircle(p,qP)) && ((idPL== 1)||(idPL==3)) && getSquare((int)m,(int)nP)!=null)){
                            currentPlayer = 1;
                            currentTV.setText("Next turn: Player 2");
                        }
                    }
        }
    }


    public boolean isKinged (CircleCentre c){

          if( c.getY() == (individualSquareBoxSize/2) || c.getY()== (7.5 * individualSquareBoxSize) ){
              return true;
          }
          else {
              return false;
          }
    }

    public boolean isPossibilityRed(){
        float XP;
        float XN;
        float doubleXP;
        float doubleXN;
        float forwardY;
        float backwardY;
        float forwardDoubleY;
        float backwardDoubleY;

        for(CircleCentre c: circleCordinates) {

                XP = c.getX() + individualSquareBoxSize;
                XN = c.getX() - individualSquareBoxSize;
                doubleXP = c.getX() + (2 * individualSquareBoxSize);
                doubleXN = c.getX() - (2 * individualSquareBoxSize);
                forwardY = c.getY() + individualSquareBoxSize;
                forwardDoubleY = c.getY() + (2 * individualSquareBoxSize);
                backwardY = c.getY() - individualSquareBoxSize;
                backwardDoubleY = c.getY() - (2 * individualSquareBoxSize);
                CircleCentre circleLeft;
                CircleCentre circleRight;
                int idR = 0;
                int idL = 0;
                int idRK = 0;
                int idLK = 0;


                if (c.getId() == 1) {
                    if (getCapturedSquare(XP, forwardY) != null) {
                        circleRight = getCapturedSquare(XP, forwardY);
                        idR = circleRight.getId();
                    }
                    if (getCapturedSquare(XN, forwardY) != null) {
                        circleLeft = getCapturedSquare(XN, forwardY);
                        idL = circleLeft.getId();
                    }

                    if (((!compareCircle(doubleXP, forwardDoubleY)) && (compareCircle(XP, forwardY)) && ((idR == 2) || (idR == 4)) && (getSquare((int) doubleXP, (int) forwardDoubleY) != null))
                            || ((!compareCircle(doubleXN, forwardDoubleY)) && (compareCircle(XN, forwardY)) && ((idL == 2) || (idL == 4)) && getSquare((int) doubleXN, (int) forwardDoubleY) != null)) {
                       return true;
                    }
                }
        }
        return  false;
    }

    public boolean isPosibilityRedKing(){
        float XP;
        float XN;
        float doubleXP;
        float doubleXN;
        float forwardY;
        float backwardY;
        float forwardDoubleY;
        float backwardDoubleY;

        for(CircleCentre c: circleCordinates) {

            XP = c.getX() + individualSquareBoxSize;
            XN = c.getX() - individualSquareBoxSize;
            doubleXP = c.getX() + (2 * individualSquareBoxSize);
            doubleXN = c.getX() - (2 * individualSquareBoxSize);
            forwardY = c.getY() + individualSquareBoxSize;
            forwardDoubleY = c.getY() + (2 * individualSquareBoxSize);
            backwardY = c.getY() - individualSquareBoxSize;
            backwardDoubleY = c.getY() - (2 * individualSquareBoxSize);
            CircleCentre circleLeft;
            CircleCentre circleRight;
            int idR = 0;
            int idL = 0;
            int idRK = 0;
            int idLK = 0;

             if (c.getId() == 3) {
                if (getCapturedSquare(XP, forwardY) != null) {
                    circleRight = getCapturedSquare(XP, forwardY);
                    idR = circleRight.getId();
                }
                if (getCapturedSquare(XN, forwardY) != null) {
                    circleLeft = getCapturedSquare(XN, forwardY);
                    idL = circleLeft.getId();
                }
                if (getCapturedSquare(XP, backwardY) != null) {
                    circleRight = getCapturedSquare(XP, backwardY);
                    idRK = circleRight.getId();
                }
                if (getCapturedSquare(XN, backwardY) != null) {
                    circleLeft = getCapturedSquare(XN, backwardY);
                    idLK = circleLeft.getId();
                }

                if (((!compareCircle(doubleXP, forwardDoubleY)) && (compareCircle(XP, forwardY)) && ((idR == 2) || (idR == 4)) && (getSquare((int) doubleXP, (int) forwardDoubleY) != null))
                        || ((!compareCircle(doubleXN, forwardDoubleY)) && (compareCircle(XN, forwardY)) && ((idL == 2) || (idL == 4)) && getSquare((int) doubleXN, (int) forwardDoubleY) != null)) {
                    return true;

                } else if (((!compareCircle(doubleXP, backwardDoubleY)) && (compareCircle(XP, backwardY)) && ((idRK == 2) || (idRK == 4)) && (getSquare((int) doubleXP, (int) backwardDoubleY) != null))
                        || (((!compareCircle(doubleXN, backwardDoubleY)) && (compareCircle(XN, backwardY))) && ((idLK == 2) || (idLK == 4)) && (getSquare((int) doubleXN, (int) backwardDoubleY) != null))) {
                    return true;
                }
            }
        }
        return false;
    }


    public boolean isPosibilityGreen(){
        float XP;
        float XN;
        float doubleXP;
        float doubleXN;
        float forwardY;
        float backwardY;
        float forwardDoubleY;
        float backwardDoubleY;

        for(CircleCentre c: circleCordinates) {

                XP = c.getX() + individualSquareBoxSize;
                XN = c.getX() - individualSquareBoxSize;
                doubleXP = c.getX() + (2 * individualSquareBoxSize);
                doubleXN = c.getX() - (2 * individualSquareBoxSize);
                forwardY = c.getY() + individualSquareBoxSize;
                forwardDoubleY = c.getY() + (2 * individualSquareBoxSize);
                backwardY = c.getY() - individualSquareBoxSize;
                backwardDoubleY = c.getY() - (2 * individualSquareBoxSize);
                CircleCentre circleLeft;
                CircleCentre circleRight;
                int idR = 0;
                int idL = 0;
                int idRK = 0;
                int idLK = 0;

             if (c.getId() == 2) {
                if (getCapturedSquare(XP, backwardY) != null) {
                    circleRight = getCapturedSquare(XP, backwardY);
                    idR = circleRight.getId();
                }
                if (getCapturedSquare(XN, backwardY) != null) {
                    circleLeft = getCapturedSquare(XN, backwardY);
                    idL = circleLeft.getId();
                }

                if (((!compareCircle(doubleXP, backwardDoubleY)) && (compareCircle(XP, backwardY)) && ((idR == 1) || (idR == 3)) && (getSquare((int) doubleXP, (int) backwardDoubleY) != null))
                        || (((!compareCircle(doubleXN, backwardDoubleY)) && (compareCircle(XN, backwardY))) && ((idL == 1) || (idL == 3)) && (getSquare((int) doubleXN, (int) backwardDoubleY) != null))) {
                    return true;

                }
             }
            }
        return false;
    }

    public boolean isPosibilityGreenKing(){
        float XP;
        float XN;
        float doubleXP;
        float doubleXN;
        float forwardY;
        float backwardY;
        float forwardDoubleY;
        float backwardDoubleY;

        for(CircleCentre c: circleCordinates) {

            XP = c.getX() + individualSquareBoxSize;
            XN = c.getX() - individualSquareBoxSize;
            doubleXP = c.getX() + (2 * individualSquareBoxSize);
            doubleXN = c.getX() - (2 * individualSquareBoxSize);
            forwardY = c.getY() + individualSquareBoxSize;
            forwardDoubleY = c.getY() + (2 * individualSquareBoxSize);
            backwardY = c.getY() - individualSquareBoxSize;
            backwardDoubleY = c.getY() - (2 * individualSquareBoxSize);
            CircleCentre circleLeft;
            CircleCentre circleRight;
            int idR = 0;
            int idL = 0;
            int idRK = 0;
            int idLK = 0;

             if (c.getId() == 4) {
                if (getCapturedSquare(XP, backwardY) != null) {
                    circleRight = getCapturedSquare(XP, backwardY);
                    idR = circleRight.getId();
                }
                if (getCapturedSquare(XN, backwardY) != null) {
                    circleLeft = getCapturedSquare(XN, backwardY);
                    idL = circleLeft.getId();
                }
                if (getCapturedSquare(XP, forwardY) != null) {
                    circleRight = getCapturedSquare(XP, forwardY);
                    idRK = circleRight.getId();
                }
                if (getCapturedSquare(XN, forwardY) != null) {
                    circleLeft = getCapturedSquare(XN, forwardY);
                    idLK = circleLeft.getId();
                }

                if (((!compareCircle(doubleXP, backwardDoubleY)) && (compareCircle(XP, backwardY)) && ((idR == 1) || (idR == 3)) && (getSquare((int) doubleXP, (int) backwardDoubleY) != null))
                        || ((!compareCircle(doubleXN, backwardDoubleY)) && (compareCircle(XN, backwardY)) && ((idL == 1) || (idL == 3)) && (getSquare((int) doubleXN, (int) backwardDoubleY) != null))) {
                    return true;

                } else if ((((!compareCircle(doubleXP, forwardDoubleY)) && (compareCircle(XP, forwardY))) && ((idRK == 1) || (idRK == 3)) && (getSquare((int) doubleXP, (int) forwardDoubleY) != null))
                        || ((!compareCircle(doubleXN, forwardDoubleY)) && (compareCircle(XN, forwardY)) && ((idLK == 1) || (idLK == 3)) && getSquare((int) doubleXN, (int) forwardDoubleY) != null)) {
                    return true;

                }
            }
        }

        return false;
    }



    public void RedrawCircles(Canvas canvas) {

        if (!alreadyDrawnFirst) {
            drawPieces(canvas);
            alreadyDrawnFirst = true;
        }   else    {

            if (oldCircleCenter != null && newCircleCenter != null) {

                if (oldCircleCenter.length() > 0 && newCircleCenter.length() > 0)  {

                    u = Float.parseFloat(oldCircleCenter.split("\\|")[0]);
                    v = Float.parseFloat(oldCircleCenter.split("\\|")[1]);
                    nu = Float.parseFloat(newCircleCenter.split("\\|")[0]);
                    nv = Float.parseFloat(newCircleCenter.split("\\|")[1]);
                    float directionX = nu-u;
                    float directionY = nv-v;

                    if(directionY> 0) {
                        capturedCircleCenter = (directionX > 0)?(u + individualSquareBoxSize) + "|" + (v + individualSquareBoxSize) : (u - individualSquareBoxSize) + "|" + (v + individualSquareBoxSize);
                    }
                    else{
                        capturedCircleCenter = (directionX >0)?(u + individualSquareBoxSize) + "|" + (v - individualSquareBoxSize):  (u - individualSquareBoxSize) + "|" + (v - individualSquareBoxSize);
                    }

                    cu = Float.parseFloat(capturedCircleCenter.split("\\|")[0]);
                    cv = Float.parseFloat(capturedCircleCenter.split("\\|")[1]);

                    for (CircleCentre c : circleCordinates) {
                       if ((c.getX() == u) && (c.getY() == v)) {

                           //simple move for player 1
                           if (c.getId() == 1 && currentPlayer != 1 && nv == v + individualSquareBoxSize && !isPossibilityRed() && !isPosibilityRedKing() ) {
                                        simpleMovePlayer1(c, u, v, nu, nv);
                           //simple move for player 2
                           }else if(c.getId()==2 && currentPlayer != 2 && nv == v - individualSquareBoxSize && !isPosibilityGreen() && !isPosibilityGreenKing() ){
                                        simpleMovePlayer2(c,u,v,nu,nv);
                           }
                           //simple move for king 1
                           else if(((c.getId() == 3) && (currentPlayer !=1)) && (nv == v + individualSquareBoxSize || nv == v - individualSquareBoxSize) && !isPossibilityRed() && !isPosibilityRedKing()){
                               //simpleMoveKing1(c,u,v,nu,nv);
                               simpleMovePlayer1(c, u, v, nu, nv);
                           }
                           //simple move for king 2
                           else if((c.getId() == 4 && currentPlayer !=2) && (nv == v + individualSquareBoxSize || nv == v - individualSquareBoxSize) && !isPosibilityGreen() && !isPosibilityGreenKing()){
                              // simpleMoveKing2(c,u,v,nu,nv);
                               simpleMovePlayer2(c,u,v,nu,nv);
                           }

                           //captured move for player1
                           if(c.getId()==1 && currentPlayer != 1 && (nv == v+ (2*individualSquareBoxSize)) && compareCircle(cu,cv) ){
                               captureMovePlayer1(c,u,v,nu,nv,cu,cv);
                           }
                           //captured move for player2Color
                           else if (c.getId()==2 && currentPlayer != 2 && (nv == v - (2*individualSquareBoxSize)) && compareCircle(cu,cv) ){
                               captureMovePlayer2(c,u,v,nu,nv,cu,cv);
                           }
                           //capture for King 1
                           else if(c.getId()==3 && currentPlayer !=1 && compareCircle(cu,cv) && (nv == v+ (2*individualSquareBoxSize) || nv == v - (2*individualSquareBoxSize)) ){
                               captureMovePlayer1(c,u,v,nu,nv,cu,cv);
                           }
                           //capture for King 2
                           else if(c.getId()==4 && currentPlayer !=2 && compareCircle(cu,cv) && (nv == v+ (2*individualSquareBoxSize) || nv == v - (2*individualSquareBoxSize)) ){
                               captureMovePlayer2(c,u,v,nu,nv,cu,cv);
                           }
                           break;
                       }
                    }
                }
            }

            for (CircleCentre c : circleCordinates) {

                canvas.drawCircle(c.getX(), c.getY(), individualSquareBoxSize / 3, c.getPaint());
            }
        }
    }

    public boolean compareCircle(float x, float y){


        for(CircleCentre circle : circleCordinates){
            count++;

            float xCordinate = circle.getX();
            float yCordinate = circle.getY();

            if(x == xCordinate && y== yCordinate ){
                return true;
            }
        }
          return false;
    }


    public Rect getSquare(int x, int y) {
        Rect r = null;
        for (Rect square : squares) {
            if (square.contains(x, y)) {
                r = square;
            }
        }
        return r;
    }

    public void setText(TextView currentPlayerTV){
        currentTV = currentPlayerTV;
    }

    public void setp1Text(TextView p1CountTV)
    {
        p1TV = p1CountTV;
    }

    public void setp2Text(TextView p2CountTV){
        p2TV = p2CountTV;
    }



    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int size = 0;
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int widthWithoutPadding = width - getPaddingLeft() - getPaddingRight();
        int heightWithoutPadding = height - getPaddingTop() - getPaddingBottom();

        // set the dimensions
        if (widthWithoutPadding > heightWithoutPadding) {


            size = heightWithoutPadding;
        } else {
            size = widthWithoutPadding;
        }
        individualSquareBoxSize = size / 8;
        viewWidth = size + getPaddingLeft() + getPaddingRight();

        setMeasuredDimension(size + getPaddingLeft() + getPaddingRight(), size + getPaddingTop() + getPaddingBottom());
    }


    //method to handle the touches from the user
    public boolean onTouchEvent(MotionEvent event) {


        //when user first touches the screen
        if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {

            int pointer_id = event.getPointerId(event.getActionIndex());
            touches[pointer_id] = true;
            touchx[pointer_id] = event.getX();
            touchy[pointer_id] = event.getY();
            touching = true;
            first = pointer_id;
            touchRect = getSquare((int) touchx[0], (int) touchy[0]);
          //  touchCircle = getCircle((touchx[0], touchy[0]);

            invalidate();
            return true;

        } else if (event.getActionMasked() == MotionEvent.ACTION_UP) {
            int pointer_id = event.getPointerId(event.getActionIndex());
            touches[pointer_id] = false;
            first = pointer_id;
            touching = false;
            invalidate();
            return true;

        }
        return super.onTouchEvent(event);
    }

}

   class  CircleCentre {
    public float x;
       public float y;
       public Paint paint;
       public int id;

       public int getId(){
           return id;
       }


       public void setId(int id){
           this.id = id;
       }

       public float getX(){
           return x;
       }

       public void setX(float x){
           this.x = x;
       }

       public Paint getPaint()  {
           return paint;
       }

       public void setPaint(Paint p)    {
           this.paint = p;
       }

       public float getY(){
           return y;
       }

       public void setY(float y) {
           this.y = y;
       }

}
