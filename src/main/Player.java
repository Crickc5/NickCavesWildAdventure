
public class Player extends Entity {

	private Weapon equipped;
	private Effect curEffect;
	private Item[] Inventory;
	public Player(boolean turnUsed, int[] hp, int[] atk, int[] def, int[] spd, Tile curTile) {
		super(turnUsed, hp, atk, def, spd, curTile);
		// TODO Auto-generated constructor stub
	}
	public void attack(Enemy e){
		
	}
}
