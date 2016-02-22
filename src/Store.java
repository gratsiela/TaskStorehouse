import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

class Store extends Storehouse{
	// 4.
	// Да се създадат три обекта, които описват магазини за продажба на стоки.
	// Магазините имат
	// продукти в наличност, като типовете и информацията за всеки продукт е
	// аналогична на тези в
	// склада. Магазинът е отговорен за това да вземе стока от склада, когато
	// количеството й падне
	// под определен минимален праг за този магазин.
	private Storehouse storehouse;

	Store(String name, Storehouse storehouse, Integer numberOfProductsOfOneSubtypeThatCanBeSold,
			Integer minNumberOfProductsOfOneSubtype) {
		super(name, numberOfProductsOfOneSubtypeThatCanBeSold, minNumberOfProductsOfOneSubtype,
				storehouse.getNumberOfProductsOfOneSubtypeThatCanBeSold());
		this.storehouse = storehouse;
	}

	@Override
	synchronized void addProductsInStorehouse() {
		while (returnListOfProductsWithLimitedAvailability().size() == 0) {
			notifyAll();// -//-//
			try {
				wait();
			} catch (InterruptedException e) {
			}
		}
		for (Map.Entry<String, TreeMap<String, Integer>> entry1 : super.products.entrySet()) {
			for (Map.Entry<String, Integer> entry2 : entry1.getValue().entrySet()) {
				if (super.returnListOfProductsWithLimitedAvailability().contains(entry2.getKey().toLowerCase())) {
					this.storehouse.getProductFromStorehouse(entry2.getKey());// за да се зареди магазина, трябва да се вземе продукт от склада
					entry2.setValue(entry2.getValue() + this.storehouse.getNumberOfProductsOfOneSubtypeThatCanBeSold());
				}
			}
		}
		System.out.println("Products supplied in " + super.name + ".");
	}

}
