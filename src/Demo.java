import java.util.ArrayList;
import java.util.Random;

class Demo {

	// !!! конзолата не представя точно случващото се с нишките

	static Storehouse storehouse = new Storehouse("SH", 5, 5, 25);
	static Store store1 = new Store("Store1", storehouse, 1, 1);
	static Store store2 = new Store("Store2", storehouse, 1, 1);
	static Store store3 = new Store("Store3", storehouse, 1, 1);
	static ArrayList<String> products = new ArrayList<String>();

	public static void main(String[] args) {
		products.add("banana");
		products.add("orange");
		products.add("apple");
		products.add("potato");
		products.add("eggplant");
		products.add("cucumber");
		products.add("pork");
		products.add("beef");
		products.add("chicken");
		new Thread(new SupplierStorehouse("supplierStorehouse")).start();
		new Thread(new SupplierShop("supplierStore1", store1)).start();
		new Thread(new SupplierShop("supplierStore2", store2)).start();
		new Thread(new SupplierShop("supplierStore3", store3)).start();
		new Thread(new Client("client1Store1", store1)).start();
		new Thread(new Client("client2Store1", store1)).start();
		new Thread(new Client("client3Store1", store1)).start();
		new Thread(new Client("client1Store2", store2)).start();
		new Thread(new Client("client2Store2", store2)).start();
		new Thread(new Client("client3Store2", store2)).start();
		new Thread(new Client("client1Store3", store3)).start();
		new Thread(new Client("client2Store3", store3)).start();
		new Thread(new Client("client3Store3", store3)).start();

	}

	// 3.
	// Да се създаде обект, който описва доставчик на стоки в склада.
	// Доставчикът е отговорен да
	// захранва склада с дефицитни стоки веднага щом някоя стока падне под
	// минималния праг за
	// склада.
	static class SupplierStorehouse implements Runnable {
		private String name;

		SupplierStorehouse(String name) {
			this.name = name;
		}

		@Override
		public void run() {
			while (true) {
				storehouse.addProductsInStorehouse();
			}
		}
	}

	static class SupplierShop implements Runnable {
		private String name;
		private Store store;

		SupplierShop(String name, Store store) {
			this.name = name;
			this.store = store;
		}

		@Override
		public void run() {
			while (true) {
				store.addProductsInStorehouse();
			}
		}
	}

	static class Client implements Runnable {
		private String name;
		private Store store;

		Client(String name, Store store) {
			this.name = name;
			this.store = store;
		}

		@Override
		public void run() {
			while (true) {
				Random r = new Random();
				int randomProduct = r.nextInt(products.size() - 1);
				this.store.getProductFromStorehouse(products.get(randomProduct));
			}
		}
	}
}
