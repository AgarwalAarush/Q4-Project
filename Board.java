public class Board {

    private final int screen_size = 1600;
		
    private int[][] foodGrid = new int[screen_size / 25][screen_size / 25];
    
    public Board(){
        developFood();
    }

    private void developFood() {
        for (int i = 0; i < foodGrid.length; i++){
            for (int j = 0; j < foodGrid[i].length; j++){
                double rand = Math.random();
                if (rand < 0.2){
                    foodGrid[i][j] = 0;
                } else{
                    foodGrid[i][j] = 1;
                }
            }
        }
    }
    
    public String toString() {
        String ans = "F";
        for (int i = 0; i < foodGrid.length; i++) {
            for (int j = 0; j < foodGrid[i].length; j++) {
                ans += foodGrid[i][j];
            }
        }
        return ans;
    }

    public void remove(int y, int x) {
        this.foodGrid[y][x] = 0;
    }

    public void foodPopulation() {
        // filling up 40% of unfilled food spaces
        for (int i = 0; i < foodGrid.length; i++){
            for (int j = 0; j < foodGrid[i].length; j++) {
                if (foodGrid[i][j] == 0) {
                    double rand = Math.random();
                    if (rand < 0.4) {
                        foodGrid[i][j] = 1;
                    }
                }
            }
        }
    }

}