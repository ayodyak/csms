package org.computerspareparts.csms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.CommandLineRunner;
import org.springframework.beans.factory.annotation.Autowired;

import Entity.*;
import Repository.*;
import java.time.LocalDate;

@SpringBootApplication(scanBasePackages = {"org.computerspareparts.csms", "Controller", "Service", "Repository", "Entity"})
@EnableJpaRepositories(basePackages = {"Repository"})
@EntityScan(basePackages = {"Entity"})
public class CsmsApplication implements CommandLineRunner {

	@Autowired
	private SupplierRepository supplierRepository;
	@Autowired
	private PurchaseRequestRepository purchaseRequestRepository;
	@Autowired
	private DeliveryRepository deliveryRepository;
	@Autowired
	private SupplierPaymentRepository supplierPaymentRepository;
	@Autowired
	private ProductRepository productRepository;

	public static void main(String[] args) {
		SpringApplication.run(CsmsApplication.class, args);
	}

	@Override
    public void run(String... args) throws Exception {
        // Check if data already exists to avoid reseeding
        if (purchaseRequestRepository.count() > 0) {
            System.out.println("Sample data already present. Skipping seeding.");
            return;
        }
        System.out.println("Seeding new data...");
		// Create sample suppliers
        Supplier supplier1 = supplierRepository.findByEmail("contact@techparts.com").orElseGet(() -> {
            Supplier s = new Supplier();
            s.setName("TechParts Ltd");
            s.setEmail("contact@techparts.com");
            s.setContactNo("+1-555-0101");
            s.setAddress("123 Tech Street, Silicon Valley, CA");
            s.setPasswordHash("password123");
            return supplierRepository.save(s);
        });

        Supplier supplier2 = supplierRepository.findByEmail("sales@compcomponents.com").orElseGet(() -> {
            Supplier s = new Supplier();
            s.setName("Computer Components Inc");
            s.setEmail("sales@compcomponents.com");
            s.setContactNo("+1-555-0102");
            s.setAddress("456 Hardware Ave, Austin, TX");
            s.setPasswordHash("password456");
            return supplierRepository.save(s);
        });

        Supplier supplier3 = supplierRepository.findByEmail("info@electronicsupply.com").orElseGet(() -> {
            Supplier s = new Supplier();
            s.setName("Electronics Supply Co");
            s.setEmail("info@electronicsupply.com");
            s.setContactNo("+1-555-0103");
            s.setAddress("789 Circuit Blvd, Seattle, WA");
            s.setPasswordHash("password789");
            return supplierRepository.save(s);
        });

        // Create and persist sample products first so FKs are valid (skip if already present)
        Product product1 = productRepository.findBySku("CPU-INTEL-I7-12700K").orElseGet(() -> {
            Product p = new Product();
            p.setName("Intel Core i7 Processor");
            p.setSku("CPU-INTEL-I7-12700K");
            return productRepository.save(p);
        });

        Product product2 = productRepository.findBySku("GPU-NVIDIA-RTX4080").orElseGet(() -> {
            Product p = new Product();
            p.setName("NVIDIA RTX 4080 Graphics Card");
            p.setSku("GPU-NVIDIA-RTX4080");
            return productRepository.save(p);
        });

        Product product3 = productRepository.findBySku("SSD-SAMSUNG-1TB").orElseGet(() -> {
            Product p = new Product();
            p.setName("Samsung 1TB SSD");
            p.setSku("SSD-SAMSUNG-1TB");
            return productRepository.save(p);
        });

        Product product4 = productRepository.findBySku("RAM-CORSAIR-32GB").orElseGet(() -> {
            Product p = new Product();
            p.setName("Corsair 32GB DDR4 RAM");
            p.setSku("RAM-CORSAIR-32GB");
            return productRepository.save(p);
        });

        Product product5 = productRepository.findBySku("MOBO-ASUS-Z690").orElseGet(() -> {
            Product p = new Product();
            p.setName("ASUS Z690 Motherboard");
            p.setSku("MOBO-ASUS-Z690");
            return productRepository.save(p);
        });

        Product product6 = productRepository.findBySku("PSU-EVGA-850W").orElseGet(() -> {
            Product p = new Product();
            p.setName("EVGA 850W Power Supply");
            p.setSku("PSU-EVGA-850W");
            return productRepository.save(p);
        });

        Product product7 = productRepository.findBySku("CASE-FRACTAL-MESH").orElseGet(() -> {
            Product p = new Product();
            p.setName("Fractal Design Meshify C Case");
            p.setSku("CASE-FRACTAL-MESH");
            return productRepository.save(p);
        });

        Product product8 = productRepository.findBySku("COOLER-NOCTUA-NH15").orElseGet(() -> {
            Product p = new Product();
            p.setName("Noctua NH-D15 CPU Cooler");
            p.setSku("COOLER-NOCTUA-NH15");
            return productRepository.save(p);
        });

        Product product9 = productRepository.findBySku("MONITOR-DELL-27").orElseGet(() -> {
            Product p = new Product();
            p.setName("Dell 27-inch 4K Monitor");
            p.setSku("MONITOR-DELL-27");
            return productRepository.save(p);
        });

        Product product10 = productRepository.findBySku("KEYBOARD-LOGITECH-MX").orElseGet(() -> {
            Product p = new Product();
            p.setName("Logitech MX Keys Keyboard");
            p.setSku("KEYBOARD-LOGITECH-MX");
            return productRepository.save(p);
        });

		// Create sample purchase requests referencing persisted products
		PurchaseRequest request1 = new PurchaseRequest();
		request1.setProduct(product1);
		request1.setSupplier(supplier1);
		request1.setQuantity(10);
		request1.setStatus("Pending");
		request1.setRequestDate(LocalDate.now().minusDays(5));
		purchaseRequestRepository.save(request1);

		PurchaseRequest request2 = new PurchaseRequest();
		request2.setProduct(product2);
		request2.setSupplier(supplier2);
		request2.setQuantity(5);
		request2.setStatus("Accepted");
		request2.setRequestDate(LocalDate.now().minusDays(3));
		purchaseRequestRepository.save(request2);

		PurchaseRequest request3 = new PurchaseRequest();
		request3.setProduct(product3);
		request3.setSupplier(supplier3);
		request3.setQuantity(20);
		request3.setStatus("Pending");
		request3.setRequestDate(LocalDate.now().minusDays(1));
		purchaseRequestRepository.save(request3);

		PurchaseRequest request4 = new PurchaseRequest();
		request4.setProduct(product1);
		request4.setSupplier(supplier2);
		request4.setQuantity(15);
		request4.setStatus("Rejected");
		request4.setRequestDate(LocalDate.now().minusDays(7));
		purchaseRequestRepository.save(request4);

		PurchaseRequest request5 = new PurchaseRequest();
		request5.setProduct(product2);
		request5.setSupplier(supplier1);
		request5.setQuantity(3);
		request5.setStatus("Received");
		request5.setRequestDate(LocalDate.now().minusDays(10));
		purchaseRequestRepository.save(request5);

		// Add 5 more purchase requests to reach 10 total
		PurchaseRequest request6 = new PurchaseRequest();
		request6.setProduct(product4);
		request6.setSupplier(supplier2);
		request6.setQuantity(8);
		request6.setStatus("Pending");
		request6.setRequestDate(LocalDate.now().minusDays(2));
		purchaseRequestRepository.save(request6);

		PurchaseRequest request7 = new PurchaseRequest();
		request7.setProduct(product5);
		request7.setSupplier(supplier3);
		request7.setQuantity(12);
		request7.setStatus("Accepted");
		request7.setRequestDate(LocalDate.now().minusDays(4));
		purchaseRequestRepository.save(request7);

		PurchaseRequest request8 = new PurchaseRequest();
		request8.setProduct(product6);
		request8.setSupplier(supplier1);
		request8.setQuantity(6);
		request8.setStatus("Rejected");
		request8.setRequestDate(LocalDate.now().minusDays(6));
		purchaseRequestRepository.save(request8);

		PurchaseRequest request9 = new PurchaseRequest();
		request9.setProduct(product7);
		request9.setSupplier(supplier2);
		request9.setQuantity(4);
		request9.setStatus("Pending");
		request9.setRequestDate(LocalDate.now().minusDays(8));
		purchaseRequestRepository.save(request9);

		PurchaseRequest request10 = new PurchaseRequest();
		request10.setProduct(product8);
		request10.setSupplier(supplier3);
		request10.setQuantity(7);
		request10.setStatus("Accepted");
		request10.setRequestDate(LocalDate.now().minusDays(9));
		purchaseRequestRepository.save(request10);

		PurchaseRequest request11 = new PurchaseRequest();
		request11.setProduct(product9);
		request11.setSupplier(supplier1);
		request11.setQuantity(2);
		request11.setStatus("Received");
		request11.setRequestDate(LocalDate.now().minusDays(11));
		purchaseRequestRepository.save(request11);

		PurchaseRequest request12 = new PurchaseRequest();
		request12.setProduct(product10);
		request12.setSupplier(supplier2);
		request12.setQuantity(15);
		request12.setStatus("Pending");
		request12.setRequestDate(LocalDate.now().minusDays(12));
		purchaseRequestRepository.save(request12);

		PurchaseRequest request13 = new PurchaseRequest();
		request13.setProduct(product1);
		request13.setSupplier(supplier3);
		request13.setQuantity(9);
		request13.setStatus("Accepted");
		request13.setRequestDate(LocalDate.now().minusDays(13));
		purchaseRequestRepository.save(request13);

		PurchaseRequest request14 = new PurchaseRequest();
		request14.setProduct(product2);
		request14.setSupplier(supplier1);
		request14.setQuantity(11);
		request14.setStatus("Rejected");
		request14.setRequestDate(LocalDate.now().minusDays(14));
		purchaseRequestRepository.save(request14);

		// Create sample payments
		SupplierPayment payment1 = new SupplierPayment();
		payment1.setPurchaseRequest(request1);
		payment1.setSupplier(supplier1);
		payment1.setAmount(150.00);
		payment1.setPaymentDate(LocalDate.now().minusDays(2));
		payment1.setStatus("Paid");
		supplierPaymentRepository.save(payment1);

		SupplierPayment payment2 = new SupplierPayment();
		payment2.setPurchaseRequest(request2);
		payment2.setSupplier(supplier2);
		payment2.setAmount(275.50);
		payment2.setPaymentDate(LocalDate.now().minusDays(1));
		payment2.setStatus("Paid");
		supplierPaymentRepository.save(payment2);

		SupplierPayment payment3 = new SupplierPayment();
		payment3.setPurchaseRequest(request3);
		payment3.setSupplier(supplier3);
		payment3.setAmount(320.75);
		payment3.setPaymentDate(LocalDate.now());
		payment3.setStatus("Pending");
		supplierPaymentRepository.save(payment3);

		SupplierPayment payment4 = new SupplierPayment();
		payment4.setPurchaseRequest(request4);
		payment4.setSupplier(supplier2);
		payment4.setAmount(180.25);
		payment4.setPaymentDate(LocalDate.now().minusDays(3));
		payment4.setStatus("Failed");
		supplierPaymentRepository.save(payment4);

		SupplierPayment payment5 = new SupplierPayment();
		payment5.setPurchaseRequest(request5);
		payment5.setSupplier(supplier1);
		payment5.setAmount(95.00);
		payment5.setPaymentDate(LocalDate.now().minusDays(4));
		payment5.setStatus("Paid");
		supplierPaymentRepository.save(payment5);

		SupplierPayment payment6 = new SupplierPayment();
		payment6.setPurchaseRequest(request6);
		payment6.setSupplier(supplier2);
		payment6.setAmount(240.80);
		payment6.setPaymentDate(LocalDate.now().minusDays(5));
		payment6.setStatus("Paid");
		supplierPaymentRepository.save(payment6);

		SupplierPayment payment7 = new SupplierPayment();
		payment7.setPurchaseRequest(request7);
		payment7.setSupplier(supplier3);
		payment7.setAmount(450.00);
		payment7.setPaymentDate(LocalDate.now().minusDays(6));
		payment7.setStatus("Paid");
		supplierPaymentRepository.save(payment7);

		SupplierPayment payment8 = new SupplierPayment();
		payment8.setPurchaseRequest(request8);
		payment8.setSupplier(supplier1);
		payment8.setAmount(125.50);
		payment8.setPaymentDate(LocalDate.now().minusDays(7));
		payment8.setStatus("Pending");
		supplierPaymentRepository.save(payment8);

		SupplierPayment payment9 = new SupplierPayment();
		payment9.setPurchaseRequest(request9);
		payment9.setSupplier(supplier2);
		payment9.setAmount(380.25);
		payment9.setPaymentDate(LocalDate.now().minusDays(8));
		payment9.setStatus("Paid");
		supplierPaymentRepository.save(payment9);

		SupplierPayment payment10 = new SupplierPayment();
		payment10.setPurchaseRequest(request10);
		payment10.setSupplier(supplier3);
		payment10.setAmount(290.75);
		payment10.setPaymentDate(LocalDate.now().minusDays(9));
		payment10.setStatus("Failed");
		supplierPaymentRepository.save(payment10);

		System.out.println("Sample data created successfully!");
	}

}
