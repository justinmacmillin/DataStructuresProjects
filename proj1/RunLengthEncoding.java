/* RunLengthEncoding.java */

/**
 *  The RunLengthEncoding class defines an object that run-length encodes an
 *  Ocean object.  Descriptions of the methods you must implement appear below.
 *  They include constructors of the form
 *
 *      public RunLengthEncoding(int i, int j, int starveTime);
 *      public RunLengthEncoding(int i, int j, int starveTime,
 *                               int[] runTypes, int[] runLengths) {
 *      public RunLengthEncoding(Ocean ocean) {
 *
 *  that create a run-length encoding of an Ocean having width i and height j,
 *  in which sharks starve after starveTime timesteps.
 *
 *  The first constructor creates a run-length encoding of an Ocean in which
 *  every cell is empty.  The second constructor creates a run-length encoding
 *  for which the runs are provided as parameters.  The third constructor
 *  converts an Ocean object into a run-length encoding of that object.
 *
 *  See the README file accompanying this project for additional details.
 */


public class RunLengthEncoding {

  /**
   *  Define any variables associated with a RunLengthEncoding object here.
   *  These variables MUST be private.
   */

  private int width;
  private int height;
  private int starvetime;
  private SList runsList = new SList();
  private SListNode followingRun;

  /**
   *  The following methods are required for Part II.
   */

  /**
   *  RunLengthEncoding() (with three parameters) is a constructor that creates
   *  a run-length encoding of an empty ocean having width i and height j,
   *  in which sharks starve after starveTime timesteps.
   *  @param i is the width of the ocean.
   *  @param j is the height of the ocean.
   *  @param starveTime is the number of timesteps sharks survive without food.
   */

  public RunLengthEncoding(int i, int j, int starveTime) {
    width = i;
    height = j;
    starvetime = starveTime;
    int[] array = new int[3];
    
    array[0] = 0;
    array[1] = (width * height);
    array[2] = starvetime;

    runsList.insertEnd(array);
    followingRun = runsList.head;
  }

  /**
   *  RunLengthEncoding() (with five parameters) is a constructor that creates
   *  a run-length encoding of an ocean having width i and height j, in which
   *  sharks starve after starveTime timesteps.  The runs of the run-length
   *  encoding are taken from two input arrays.  Run i has length runLengths[i]
   *  and species runTypes[i].
   *  @param i is the width of the ocean.
   *  @param j is the height of the ocean.
   *  @param starveTime is the number of timesteps sharks survive without food.
   *  @param runTypes is an array that represents the species represented by
   *         each run.  Each element of runTypes is Ocean.EMPTY, Ocean.FISH,
   *         or Ocean.SHARK.  Any run of sharks is treated as a run of newborn
   *         sharks (which are equivalent to sharks that have just eaten).
   *  @param runLengths is an array that represents the length of each run.
   *         The sum of all elements of the runLengths array should be i * j.
   */

  public RunLengthEncoding(int i, int j, int starveTime,
                           int[] runTypes, int[] runLengths) {
    width = i;
    height = j;
    starvetime = starveTime;
    /* [run type, run length, starvetime] */
    for (int index = 0; index < runTypes.length; index++) {
      int[] array = new int[3];
      array[0] = runTypes[index];
      array[1] = runLengths[index];
      array[2] = starveTime;
      runsList.insertEnd(array);
    }
    followingRun = runsList.head;
  }

  /**
   *  restartRuns() and nextRun() are two methods that work together to return
   *  all the runs in the run-length encoding, one by one.  Each time
   *  nextRun() is invoked, it returns a different run (represented as an
   *  array of two ints), until every run has been returned.  The first time
   *  nextRun() is invoked, it returns the first run in the encoding, which
   *  contains cell (0, 0).  After every run has been returned, nextRun()
   *  returns null, which lets the calling program know that there are no more
   *  runs in the encoding.
   *
   *  The restartRuns() method resets the enumeration, so that nextRun() will
   *  once again enumerate all the runs as if nextRun() were being invoked for
   *  the first time.
   *
   *  (Note:  Don't worry about what might happen if nextRun() is interleaved
   *  with addFish() or addShark(); it won't happen.)
   */

  /**
   *  restartRuns() resets the enumeration as described above, so that
   *  nextRun() will enumerate all the runs from the beginning.
   */

  public void restartRuns() {
    followingRun = runsList.head;
  }

  /**
   *  nextRun() returns the next run in the enumeration, as described above.
   *  If the runs have been exhausted, it returns null.  The return value is
   *  an array of two ints (constructed here), representing the type and the
   *  size of the run, in that order.
   *  @return the next run in the enumeration, represented by an array of
   *          two ints.  The int at index zero indicates the run type
   *          (Ocean.EMPTY, Ocean.SHARK, or Ocean.FISH).  The int at index one
   *          indicates the run length (which must be at least 1).
   */

  public int[] nextRun() {
    int[] a = new int[2];

    if (followingRun == null) {
      return null;
    }
    else {
      a[0] = followingRun.item[0];
      a[1] = followingRun.item[1];
      followingRun = followingRun.next;
    }

    return a;
  }

  /**
   *  toOcean() converts a run-length encoding of an ocean into an Ocean
   *  object.  You will need to implement the three-parameter addShark method
   *  in the Ocean class for this method's use.
   *  @return the Ocean represented by a run-length encoding.
   */

  public Ocean toOcean() {
    Ocean resultingOcean = new Ocean(width, height, starvetime);
    SListNode currentNode = runsList.head;
  
    int currentRow = 0;
    int currentColumn = 0;

    while (currentNode != null) {
      int type = currentNode.item[0];
      int lengthOfRun = currentNode.item[1];
      int hunger = currentNode.item[2];
      
      if (type == Ocean.EMPTY) {
        while (lengthOfRun > 0) {
          if (currentColumn > width - 1) {
            currentColumn = 0;
            currentRow++;
          } 
          currentColumn++;
          lengthOfRun--;
        }
      }
      else if (type == Ocean.FISH) {
        while (lengthOfRun > 0) {
          if (currentColumn > width - 1) {
            currentColumn = 0;
            currentRow++;
          }
          resultingOcean.addFish(currentColumn, currentRow); 
          currentColumn++;
          lengthOfRun--;
        }
      }
      else {
        while (lengthOfRun > 0) {
          if (currentColumn > width - 1) {
            currentColumn = 0;
            currentRow++;
          }
          resultingOcean.addShark(currentColumn, currentRow, hunger); 
          currentColumn++;
          lengthOfRun--;
        }
      }
      currentNode = currentNode.next;
    }
  return resultingOcean;
  }



  /**
   *  The following method is required for Part III.
   */

  /**
   *  RunLengthEncoding() (with one parameter) is a constructor that creates
   *  a run-length encoding of an input Ocean.  You will need to implement
   *  the sharkFeeding method in the Ocean class for this constructor's use.
   *  @param sea is the ocean to encode.
   */

  public RunLengthEncoding(Ocean sea) {
    int[] arrayInRun = new int[3];

    for (int index2 = 0; index2 < sea.height(); index2++) {
      for (int index1 = 0; index1 < sea.width(); index1++) {
        if (index1 == 0 && index2 == 0) {
          arrayInRun[0] = sea.cellContents(index1, index2);
          arrayInRun[1] = 1;
          if (sea.cellContents(index1, index2) == Ocean.SHARK) {
            arrayInRun[2] = sea.sharkFeeding(index1, index2);
          }
          else {
            arrayInRun[2] = 0;
          }
        } 
        else { /* this covers every case after (0,0) */
          if (sea.cellContents(index1, index2) == arrayInRun[0]) {
            if (sea.cellContents(index1, index2) == Ocean.SHARK) {
              if (sea.sharkFeeding(index1, index2) == arrayInRun[2]) {
                arrayInRun[1]++;
              }
              else { /* covers the case if the hungers of sharks are not the same */
                runsList.insertEnd(arrayInRun);
                arrayInRun = new int[3];
                arrayInRun[0] = sea.cellContents(index1, index2);
                arrayInRun[1] = 1;
                arrayInRun[2] = sea.sharkFeeding(index1, index2);
              }
            }
            else { /* If the creatures are the same but arent sharks */
              arrayInRun[1]++;
            }
          }
          else {
            runsList.insertEnd(arrayInRun);
            arrayInRun = new int[3];
            arrayInRun[0] = sea.cellContents(index1, index2);
            arrayInRun[1] = 1;
            if (sea.cellContents(index1, index2) == Ocean.SHARK) {
              arrayInRun[2] = sea.sharkFeeding(index1, index2);
            }
            else { /* if it isnt a shark */
              arrayInRun[2] = 0;
            }
          }
        }
      }
    }
    runsList.insertEnd(arrayInRun);
    width = sea.width();
    height = sea.height();
    starvetime = sea.starveTime();
    followingRun = runsList.head;
    check();
  }

  /**
   *  The following methods are required for Part IV.
   */

  /**
   *  addFish() places a fish in cell (x, y) if the cell is empty.  If the
   *  cell is already occupied, leave the cell as it is.  The final run-length
   *  encoding should be compressed as much as possible; there should not be
   *  two consecutive runs of sharks with the same degree of hunger.
   *  @param x is the x-coordinate of the cell to place a fish in.
   *  @param y is the y-coordinate of the cell to place a fish in.
   */

  public void addFish(int x, int y) {
    // Your solution here, but you should probably leave the following line
    //   at the end.
    check();
  }

  /**
   *  addShark() (with two parameters) places a newborn shark in cell (x, y) if
   *  the cell is empty.  A "newborn" shark is equivalent to a shark that has
   *  just eaten.  If the cell is already occupied, leave the cell as it is.
   *  The final run-length encoding should be compressed as much as possible;
   *  there should not be two consecutive runs of sharks with the same degree
   *  of hunger.
   *  @param x is the x-coordinate of the cell to place a shark in.
   *  @param y is the y-coordinate of the cell to place a shark in.
   */

  public void addShark(int x, int y) {
    // Your solution here, but you should probably leave the following line
    //   at the end.
    check();
  }

  /**
   *  check() walks through the run-length encoding and prints an error message
   *  if two consecutive runs have the same contents, or if the sum of all run
   *  lengths does not equal the number of cells in the ocean.
   */

  private void check() {
    SListNode currentNode = runsList.head;

    while (currentNode.next != null) {
      if (currentNode.item[0] == currentNode.next.item[0]) {
        if (currentNode.item[0] == Ocean.SHARK && 
          currentNode.item[2] != currentNode.next.item[2]) {
        }
        else {
          System.out.println("Two consecutive runs have the same contents.");
        }
      }
      currentNode = currentNode.next;
    }

    SListNode currNode = runsList.head;
    int totalNumberRunLengths = 0;  
    while (currNode != null) {
      totalNumberRunLengths = totalNumberRunLengths + currNode.item[1];
      currNode = currNode.next;
    }

    if (totalNumberRunLengths != (width * height)) {
      System.out.println("The sum of all run lengths does not equal the number of cells in the ocean.");
    }
  }


}

