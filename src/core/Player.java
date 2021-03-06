package core;

import java.util.ArrayList;

// Container for stats and state
public class Player extends Role {
	public int[] expGauges = {50, 150, 300}; //limits of upgrading

	private final int maxCapacity = 10;
	private final ArrayList<Item> inventory = new ArrayList<>();

	// STATE
	private int index = 0; // Counts from 0 - 31
	private final int id;

	public Player(int id) {
		this.id = id;

		// Init default values
		Level = 1;
		Health = 100;
		Strength = 50;
		Defense = 50;
		Agility = 5;
		Gold = 200;
		Exp = 0;

		Type = "Normal";
		Types = new String[]{"Normal", "Melee", "Mage", "Archer"};
		// Health Strength Defense Agility (Levels 1 - 4)
		Stats = new int[][][]{
			/*melee*/{{100, 200, 200, 250}, {50, 35, 35, 40}, {50, 20, 20, 25}, {5, 20, 20, 25}},
			/*mage*/{{100, 200, 200, 250}, {50, 20, 20, 25}, {50, 40, 40, 45}, {5, 15, 15, 20}},
			/*archer*/{{100, 200, 200, 250}, {50, 50, 50, 55}, {50, 30, 30, 35}, {5, 10, 10, 15}}
		};
	}

	public void move(int steps) {
		index += steps;
	}

	public int getIndex() {
		return index;
	}

	public int getId() {
		return id;
	}

	public ArrayList<Item> getItems() {
		return inventory;
	}

	public boolean addItem(Item item) {
		if (inventory.size() >= maxCapacity)
			return false;

		Strength += item.StrengthBonus;
		Defense += item.DefenseBonus;
		Agility += item.AgilityBonus;
		Agility = Math.max(0, Math.min(Agility, 90));

		return inventory.add(item);
	}

	public void removeItem(int inventoryIndex) {
		Item item = inventory.remove(inventoryIndex);

		Strength -= item.StrengthBonus;
		Defense -= item.DefenseBonus;
		Agility -= item.AgilityBonus;
		Agility = Math.max(0, Math.min(Agility, 90));
	}

	public void levelUp() {
		if (Exp < 50 || Exp > 500) {
			//dont waste runtime here
			return;
		}

		boolean upgraded = false;
		for (int i = 0; i < expGauges.length; i++) {
			if (i == Level - 1 && Exp >= expGauges[Level - 1]) {
				//just passel level gauge for the first time this level
				Level++;
				System.out.printf("%n[Player %d] upgraded to Level %d%n", getId(), Level);
				upgraded = true;
				break;//no need to continue looping
			}
		}

		if (upgraded) {
			if (Level == 2) {
				//for now allow choose type at level 2 only
				chooseType();
			}

			//check type of player: Melee = 0, Mage = 1, Archer = 2
			int typeIndex = 0;
			for (int i = 1; i < Types.length; i++) {
				if (Type.equals(Types[i])) {
					typeIndex = i - 1;
					break;
				}
			}

			//increment stats to avoid overwritting item attributes
			Health += Stats[typeIndex][0][Level - 1];
			Strength += Stats[typeIndex][1][Level - 1];
			Defense += Stats[typeIndex][2][Level - 1];
			Agility += Stats[typeIndex][3][Level - 1];
		}

	}

	public void chooseType() {
		System.out.println("Choose Type:");
		for (int i = 1; i < Types.length; i++) {
			System.out.printf("%d. %s\n", i, Types[i]);
		}

		int choiceType = CommandParser.readInt(new int[]{1, 2, 3});
		Type = Types[choiceType];
		System.out.printf("[Player %d] is now type %s\n", getId(), Type);
	}


}
