
public class Entity {
	private boolean turnUsed;
	private int[] hp, atk, def, spd;
	private Tile curTile;
	
	public Entity(boolean turnUsed, int[] hp, int[] atk, int[] def, int[] spd, Tile curTile) {
		this.turnUsed = turnUsed;
		this.hp = hp;
		this.atk = atk;
		this.def = def;
		this.spd = spd;
		this.curTile = curTile;
	}
	public boolean isTurnUsed() {
		return turnUsed;
	}
	public void setTurnUsed(boolean turnUsed) {
		this.turnUsed = turnUsed;
	}
	public int[] getHp() {
		return hp;
	}
	public void setHp(int[] hp) {
		this.hp = hp;
	}
	public int[] getAtk() {
		return atk;
	}
	public void setAtk(int[] atk) {
		this.atk = atk;
	}
	public int[] getDef() {
		return def;
	}
	public void setDef(int[] def) {
		this.def = def;
	}
	public int[] getSpd() {
		return spd;
	}
	public void setSpd(int[] spd) {
		this.spd = spd;
	}
	public Tile getCurTile() {
		return curTile;
	}
	public void setCurTile(Tile curTile) {
		this.curTile = curTile;
	}
}
