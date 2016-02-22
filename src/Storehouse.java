import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

class Storehouse {
	// 1.
	// В склада има продукти, като за всеки продукт имаме информация за име и
	// наличност в
	// склада. Продуктите са групирани по тип.
	// При създаване на склада в него трябва да има три типа продукти:
	// FRUITS, VEGETABLES, MEATS, като за всеки тип има по 15 единици наличност
	// на следните продукти:
	// •FRUITS: Banana, Orange, Apple.
	// •VEGETABLES: Potato, Eggplant, Cucumber.
	// •MEATS: Pork, Beef, Chicken

	protected String name;
	protected TreeMap<String, TreeMap<String, Integer>> products;// type, subtype, number of products
	private Integer numberOfProductsOfOneSubtypeThatCanBeSold = 5;
	private Integer minNumberOfProductsOfOneSubtype = 5;
	private Integer numberOfProductsOfOneSubtypeThatHasToBeSupliedWhenItIsNotAvailable = 25;


	Storehouse(String name, Integer numberOfProductsOfOneSubtypeThatCanBeSold, Integer minNumberOfProductsOfOneSubtype,
			Integer numberOfProductsOfOneSubtypeThatHasToBeSupliedWhenItIsNotAvailable) {
		this.name = name;
		this.products = new TreeMap<String, TreeMap<String, Integer>>();
		this.products.put("FRUITS", new TreeMap<String, Integer>());
		this.products.put("VEGETABLES", new TreeMap<String, Integer>());
		this.products.put("MEATS", new TreeMap<String, Integer>());
		this.products.get("FRUITS").put("banana", 15);
		this.products.get("FRUITS").put("orange", 15);
		this.products.get("FRUITS").put("apple", 15);
		this.products.get("VEGETABLES").put("potato", 15);
		this.products.get("VEGETABLES").put("eggplant", 15);
		this.products.get("VEGETABLES").put("cucumber", 15);
		this.products.get("MEATS").put("pork", 15);
		this.products.get("MEATS").put("beef", 15);
		this.products.get("MEATS").put("chicken", 15);
	}
	
//	2.
//	В склада трябва да има два метода
//	1. Доставка на стоки – в този метод се проверява дали в склада има наличност под 
//	указан минимален праг. Ако има такива, да се захрани склада с по 25 бройки от всеки 
//	дефицитен продукт. Ако няма дефицитни стоки, методът трябва да изчака някоя стока 
//	да се изчерпи, за да захрани склада.
//	2. Взимане на стоки – в този метод се подава име на стока и се изисква склада да захрани
//	5 бройки от тази стока. Ако стоката е дефицитна, методът трябва да изчака 
//	захранването на склада и тогава да вземе стоката.
	synchronized void addProductsInStorehouse() {
		// Докато няма продукти с наличност под минималната, се изчкава докато продукт бъде взет.
		while (returnListOfProductsWithLimitedAvailability().size() == 0) {
			// Навсякъде е notifyAll(), а не notify(), защото ако е с notify(), 
			// тъй като в един момент някой продукт от магазина ще свърши 
			// и една нишка-клиент ще изчака зареждането на продукта, събуждайки друга нишка, 
			// но другата нишка може също да е нишка-клиент, който чака за продукт и буди друга нишка, която отново може да е нишка-клиент итн.
			// notify() работи с по една нишка от всеки тип: SupplierStorehouse, SupplierStore и Client,
			// но не работи, когато има повече от една нишка от всеки тип.
			notifyAll();
			try {
				wait();
			} catch (InterruptedException e) {
			}
		}
		// когато има поне един продукт с наличност под минималната, се добавят бройки към всички продукти с наличност под минималната и се нотифицира, 
		// че може да се взима, ако някой чака да вземе продукт, който е бил с наличност под минималната
		for (Map.Entry<String, TreeMap<String, Integer>> entry1 : this.products.entrySet()) {
			for (Map.Entry<String, Integer> entry2 : entry1.getValue().entrySet()) {
				if (returnListOfProductsWithLimitedAvailability().contains(entry2.getKey().toLowerCase())) {
					entry2.setValue(entry2.getValue()
							+ this.numberOfProductsOfOneSubtypeThatHasToBeSupliedWhenItIsNotAvailable);
				}
			}
		}
		System.out.println("Products supplied in "+this.name+".");
	}

	synchronized void getProductFromStorehouse(String product) {
		// докато листа с продукти с наличност под мнималната съдържа искания продукт,
		// се изчаква докато този продукт бъде зареден
		while (returnListOfProductsWithLimitedAvailability().contains(product.toLowerCase())) {
			System.out.println("not available product in " + this.name + ": " + product);
			notifyAll();// -//-//
			try {
				wait();
			} catch (InterruptedException e) {
			}
		}
		// когато исканият продукт е в наличност >= инамалната,
		// стойността му се намалява
		for (Map.Entry<String, TreeMap<String, Integer>> entry1 : this.products.entrySet()) {
			for (Map.Entry<String, Integer> entry2 : entry1.getValue().entrySet()) {
				if (entry2.getKey().toLowerCase().equals(product.toLowerCase())) {
					entry2.setValue(entry2.getValue() - this.numberOfProductsOfOneSubtypeThatCanBeSold);
					System.out.println("sold product from " + this.name + ": " + product);
					break;
				}
			}
		}
	}

	protected TreeSet<String> returnListOfProductsWithLimitedAvailability() {
		TreeSet<String> list = new TreeSet();
		for (Map.Entry<String, TreeMap<String, Integer>> entry1 : this.products.entrySet()) {
			for (Map.Entry<String, Integer> entry2 : entry1.getValue().entrySet()) {
				if (entry2.getValue().compareTo(this.minNumberOfProductsOfOneSubtype) == -1) {
					list.add(entry2.getKey().toLowerCase());
				}
			}
		}
		return list;
	}

	Integer getNumberOfProductsOfOneSubtypeThatCanBeSold() {
		return numberOfProductsOfOneSubtypeThatCanBeSold;
	}
}
