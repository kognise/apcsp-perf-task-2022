import java.util.Scanner;

public class Main {
	public static void main(String[] args) throws InterruptedException {
		Scanner scanner = new Scanner(System.in);
		
		System.out.print("Enter dimensions in format 'x y': ");
		String[] sizeSplit = scanner.nextLine().split(" ");
		Board board = new Board(
			Integer.parseInt(sizeSplit[0]),
			Integer.parseInt(sizeSplit[1])
		);

		while (true) {
			clearScreen();
			System.out.println("Board setup...");
			board.printBoard();
			System.out.print("Toggle square at 'x y' or blank to run: ");
			String[] coordSplit = scanner.nextLine().split(" ");

			if (coordSplit.length < 2) {
				break;
			} else {
				board.toggle(
					Integer.parseInt(coordSplit[0]) - 1,
					Integer.parseInt(coordSplit[1]) - 1
				);
			}
		}

		while (true) {
			clearScreen();
			System.out.println("Running simulation... ^C to exit");
			board.printBoard();
			board.step();
			Thread.sleep(200);
		}
	}

	private static void clearScreen() {
		System.out.print("\033[H\033[2J");  
		System.out.flush();
	}
}

class Board {
	private boolean[][] matrix;
	private int w;
	private int h;

	public Board(int w, int h) {
		this.matrix = new boolean[h][w];
		this.w = w;
		this.h = h;
	}

	public void toggle(int x, int y) { this.matrix[y][x] = !this.matrix[y][x]; }

	public boolean[][] getMatrix() { return this.matrix; }
	public int getW() { return this.w; }
	public int getH() { return this.h; }

	public void printBoard() {
		for (int y = 0; y < this.h; y++) {
			for (int x = 0; x < this.w; x++) {
				if (this.matrix[y][x]) {
					System.out.print("██");
				} else {
					System.out.print("░░");
				}
			}
			System.out.print("\n");
		}
	}

	public void step() {
		boolean[][] newMatrix = new boolean[this.h][this.w];

		for (int y = 0; y < this.h; y++) {
			for (int x = 0; x < this.w; x++) {
				int liveNeighbors = this.getLiveNeighbors(x, y);

				// Any live cell with two or three live neighbours survives.
				// Any dead cell with three live neighbours becomes a live cell.
				if (this.matrix[y][x] && liveNeighbors == 2) {
					newMatrix[y][x] = true;
				} else if (liveNeighbors == 3) {
					newMatrix[y][x] = true;
				}
			}
		}

		this.matrix = newMatrix;
 	}

	private int getLiveNeighbors(int xCenter, int yCenter) {
		int liveNeighbors = 0;
		for (int y = yCenter - 1; y <= yCenter + 1; y++) {
			for (int x = xCenter - 1; x <= xCenter + 1; x++) {
				// Out-of-bounds check.
				if (y < 0 || x < 0 || y >= h || x >= w) continue;
				// We only want neighbors.
				if (x == xCenter && y == yCenter) continue;

				if (this.matrix[y][x]) liveNeighbors++;
			}
		}
		return liveNeighbors;
	}
}