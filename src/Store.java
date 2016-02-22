import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

class Store extends Storehouse{
	// 4.
	// �� �� �������� ��� ������, ����� ������� �������� �� �������� �� �����.
	// ���������� ����
	// �������� � ���������, ���� �������� � ������������ �� ����� ������� �
	// ���������� �� ���� �
	// ������. ��������� � ��������� �� ���� �� ����� ����� �� ������, ������
	// ������������ � �����
	// ��� ��������� ��������� ���� �� ���� �������.
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
					this.storehouse.getProductFromStorehouse(entry2.getKey());// �� �� �� ������ ��������, ������ �� �� ����� ������� �� ������
					entry2.setValue(entry2.getValue() + this.storehouse.getNumberOfProductsOfOneSubtypeThatCanBeSold());
				}
			}
		}
		System.out.println("Products supplied in " + super.name + ".");
	}

}
