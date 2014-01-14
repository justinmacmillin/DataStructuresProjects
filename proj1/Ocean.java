/* Ocean.java */

/**
 *  The Ocean class defines an object that models an ocean full of sharks and
 *  fish.  Descriptions of the methods you must implement appear below.  They
 *  include a constructor of the form
 *
 *      public Ocean(int i, int j, int starveTime);
 *
 *  that creates an empty ocean having width i and height j, in which sharks
 *  starve after starveTime timesteps.
 *
 *  See the README file accompanying this project for additional details.
 */

public class Ocean {

  /**
   *  Do not rename these constants.  WARNING:  if you change the numbers, you
   *  will need to recompile Test4.java.  Failure to do so will give you a very
   *  hard-to-find bug.
   */

  public final static int EMPTY = 0;
  public final static int SHARK = 1;
  public final static int FISH = 2;

  /**
   *  Define any variables associated with an Ocean object here.  These
   *  variables MUST be private.
   */

  private int width;
  private int height;
  private int starvetime;
  private int[][] ocean;
  private int[][] hunger;

  /**
   *  The following methods are required for Part I.
   */

  /**
   *  Ocean() is a constructor that creates an empty ocean having width i and
   *  height j, in which sharks starve after starveTime timesteps.
   *  @param i is the width of the ocean.
   *  @param j is the height of the ocean.
   *  @param starveTime is the number of timesteps sharks survive without food.
   */

  public Ocean(int i, int j, int starveTime) {
    width = i;
    height = j;
    starvetime = starveTime;
    ocean = new int[i][j];
    hunger = new int[i][j];
    for (int index1 = 0; index1 < width; index1++) {
      for (int index2 = 0; index2 < height; index2++) {
        ocean[index1][index2] = EMPTY;
        hunger[index1][index2] = 0;
      }
    }
  }

  /**
   *  width() returns the width of an Ocean object.
   *  @return the width of the ocean.
   */

  public int width() {
    return width;
  }

  /**
   *  height() returns the height of an Ocean object.
   *  @return the height of the ocean.
   */

  public int height() {
    return height;
  }

  /**
   *  starveTime() returns the number of timesteps sharks survive without food.
   *  @return the number of timesteps sharks survive without food.
   */

  public int starveTime() {
    return starvetime;
  }

  /**
   *  addFish() places a fish in cell (x, y) if the cell is empty.  If the
   *  cell is already occupied, leave the cell as it is.
   *  @param x is the x-coordinate of the cell to place a fish in.
   *  @param y is the y-coordinate of the cell to place a fish in.
   */

  public void addFish(int x, int y) {
    if (cellContents(x,y) == EMPTY) {
      ocean[wrapX(x)][wrapY(y)] = FISH;
    }
  }

  /**
   *  addShark() (with two parameters) places a newborn shark in cell (x, y) if
   *  the cell is empty.  A "newborn" shark is equivalent to a shark that has
   *  just eaten.  If the cell is already occupied, leave the cell as it is.
   *  @param x is the x-coordinate of the cell to place a shark in.
   *  @param y is the y-coordinate of the cell to place a shark in.
   */

  public void addShark(int x, int y) {
    if (cellContents(x,y) == EMPTY) {
      ocean[wrapX(x)][wrapY(y)] = SHARK;
      hunger[wrapX(x)][wrapY(y)] = starvetime;
    }
  }

  /**
   *  cellContents() returns EMPTY if cell (x, y) is empty, FISH if it contains
   *  a fish, and SHARK if it contains a shark.
   *  @param x is the x-coordinate of the cell whose contents are queried.
   *  @param y is the y-coordinate of the cell whose contents are queried.
   */

  public void addEmpty(int x, int y) {
    ocean[wrapX(x)][wrapY(y)]=EMPTY;
    hunger[wrapX(x)][wrapY(y)]=0;
  }
  
  public int cellContents(int x, int y) {
    return ocean[wrapX(x)][wrapY(y)];
  }

  public int wrapX(int x) {
    x = x % width;
    if (x < 0) {
      x = x + width;
    }
    return x;
  }

  public int wrapY(int y) {
    y = y % height;
    if (y < 0) {
      y = y + height;
    }
    return y;
  }


  /* RETURNS AN ARRAY OF [SHARKS, FISH] */
/*  private int[] neighbors(int x, int y) {
    int[] arrayOfNeighbors = new int[2];
    int currX, currY;
    for (int index1 = -1; index1 <= 1; index1++) {
      for (int index2 = -1; index2 <= 1; index2++) {
        currX = wrapX(x + index1);
        currY = wrapY(y + index2);
        if (currX != x && currY != y) {
         if (cellContents(currX, currY) == SHARK) {
          arrayOfNeighbors[0]++;
          }
         if (cellContents(currX, currY) == FISH) {
          arrayOfNeighbors[1]++;
          }
        }
      }
    }
    return arrayOfNeighbors;    
  } */

  private int[] neighbors(int x, int y) {

    int[] array = new int[8];
    array[0] = cellContents(x-1, y+1);
    array[1] = cellContents(x-1, y);
    array[2] = cellContents(x-1, y-1);
    array[3] = cellContents(x, y+1);
    array[4] = cellContents(x, y-1);
    array[5] = cellContents(x+1, y+1);
    array[6] = cellContents(x+1, y);
    array[7] = cellContents(x+1, y-1);

    int[] arrayOfNeighbors = new int[2];
    for (int i = 0; i < array.length; i++) {
      if (array[i] == SHARK) {
        arrayOfNeighbors[0]++;
      }
      if (array[i] == FISH) {
        arrayOfNeighbors[1]++;
      }
    }

    return arrayOfNeighbors;
  }

  /**
   *  timeStep() performs a simulation timestep as described in README.
   *  @return an ocean representing the elapse of one timestep.
   */

  public Ocean timeStep() {
    Ocean nextOcean = new Ocean(width, height, starvetime);
    int[] n;
    int sharks, fish, hungerLevel;

    for (int index2=0; index2<height; index2++){
      for (int index1=0; index1<width; index1++){
        n = neighbors(index1, index2);
        sharks = n[0];
        fish = n[1];
        hungerLevel = hunger[index1][index2];
          if (cellContents(index1, index2) == SHARK){
            if (fish > 0){
              nextOcean.addShark(index1, index2);
            }
            else {
              if (hungerLevel == 0) {
                nextOcean.addEmpty(index1, index2);                  
              }
              else {
                nextOcean.addShark(index1, index2);
                nextOcean.hunger[index1][index2] = hunger[index1][index2] - 1;
              }
            }
          }
          else if (cellContents(index1, index2) == FISH){
            if (sharks > 0){
              if (sharks == 1){
                nextOcean.addEmpty(index1, index2);
              }
              else{
                nextOcean.addShark(index1, index2);
              }
            }
            else {
              nextOcean.addFish(index1, index2);
            }
          }
          else {
            if (fish < 2) {
              nextOcean.addEmpty(index1, index2);
            }
            else if (fish >= 2 && sharks <= 1) {
              nextOcean.addFish(index1, index2);
            }
            else if (fish >= 2 && sharks >= 2){
              nextOcean.addShark(index1, index2);
            }
          }
      }
    }
    return nextOcean;
  }


  /**
   *  The following method is required for Part II.
   */

  /**
   *  addShark() (with three parameters) places a shark in cell (x, y) if the
   *  cell is empty.  The shark's hunger is represented by the third parameter.
   *  If the cell is already occupied, leave the cell as it is.  You will need
   *  this method to help convert run-length encodings to Oceans.
   *  @param x is the x-coordinate of the cell to place a shark in.
   *  @param y is the y-coordinate of the cell to place a shark in.
   *  @param feeding is an integer that indicates the shark's hunger.  You may
   *         encode it any way you want; for instance, "feeding" may be the
   *         last timestep the shark was fed, or the amount of time that has
   *         passed since the shark was last fed, or the amount of time left
   *         before the shark will starve.  It's up to you, but be consistent.
   */

  public void addShark(int x, int y, int feeding) {
    if (cellContents(x, y) == EMPTY) {
      ocean[x][y] = SHARK;
      hunger[x][y] = feeding;
    }
  }

  /**
   *  The following method is required for Part III.
   */

  /**
   *  sharkFeeding() returns an integer that indicates the hunger of the shark
   *  in cell (x, y), using the same "feeding" representation as the parameter
   *  to addShark() described above.  If cell (x, y) does not contain a shark,
   *  then its return value is undefined--that is, anything you want.
   *  Normally, this method should not be called if cell (x, y) does not
   *  contain a shark.  You will need this method to help convert Oceans to
   *  run-length encodings.
   *  @param x is the x-coordinate of the cell whose contents are queried.
   *  @param y is the y-coordinate of the cell whose contents are queried.
   */

  public int sharkFeeding(int x, int y) {
    if (ocean[x][y] == SHARK) {
      return hunger[x][y];
    }
    return 0;
  }

}