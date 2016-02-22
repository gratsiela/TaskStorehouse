import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

class Storehouse {
	// 1.
	// � ������ ��� ��������, ���� �� ����� ������� ����� ���������� �� ��� �
	// ��������� �
	// ������. ���������� �� ��������� �� ���.
	// ��� ��������� �� ������ � ���� ������ �� ��� ��� ���� ��������:
	// FRUITS, VEGETABLES, MEATS, ���� �� ����� ��� ��� �� 15 ������� ���������
	// �� �������� ��������:
	// �FRUITS: Banana, Orange, Apple.
	// �VEGETABLES: Potato, Eggplant, Cucumber.
	// �MEATS: Pork, Beef, Chicken

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
//	� ������ ������ �� ��� ��� ������
//	1. �������� �� ����� � � ���� ����� �� ��������� ���� � ������ ��� ��������� ��� 
//	������ ��������� ����. ��� ��� ������, �� �� ������� ������ � �� 25 ������ �� ����� 
//	��������� �������. ��� ���� ��������� �����, ������� ������ �� ������ ����� ����� 
//	�� �� �������, �� �� ������� ������.
//	2. ������� �� ����� � � ���� ����� �� ������ ��� �� ����� � �� ������� ������ �� �������
//	5 ������ �� ���� �����. ��� ������� � ���������, ������� ������ �� ������ 
//	������������ �� ������ � ������ �� ����� �������.
	synchronized void addProductsInStorehouse() {
		// ������ ���� �������� � ��������� ��� �����������, �� ������� ������ ������� ���� ����.
		while (returnListOfProductsWithLimitedAvailability().size() == 0) {
			// ��������� � notifyAll(), � �� notify(), ������ ��� � � notify(), 
			// ��� ���� � ���� ������ ����� ������� �� �������� �� ������ 
			// � ���� �����-������ �� ������ ����������� �� ��������, ���������� ����� �����, 
			// �� ������� ����� ���� ���� �� � �����-������, ����� ���� �� ������� � ���� ����� �����, ����� ������ ���� �� � �����-������ ���.
			// notify() ������ � �� ���� ����� �� ����� ���: SupplierStorehouse, SupplierStore � Client,
			// �� �� ������, ������ ��� ������ �� ���� ����� �� ����� ���.
			notifyAll();
			try {
				wait();
			} catch (InterruptedException e) {
			}
		}
		// ������ ��� ���� ���� ������� � ��������� ��� �����������, �� ������� ������ ��� ������ �������� � ��������� ��� ����������� � �� ����������, 
		// �� ���� �� �� �����, ��� ����� ���� �� ����� �������, ����� � ��� � ��������� ��� �����������
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
		// ������ ����� � �������� � ��������� ��� ���������� ������� ������� �������,
		// �� ������� ������ ���� ������� ���� �������
		while (returnListOfProductsWithLimitedAvailability().contains(product.toLowerCase())) {
			System.out.println("not available product in " + this.name + ": " + product);
			notifyAll();// -//-//
			try {
				wait();
			} catch (InterruptedException e) {
			}
		}
		// ������ �������� ������� � � ��������� >= ����������,
		// ���������� �� �� ��������
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
