package org.computerspareparts.csms.controller;

import org.computerspareparts.csms.entity.Part;
import org.computerspareparts.csms.entity.Supplier;
import org.computerspareparts.csms.entity.PurchaseRequest;
import org.computerspareparts.csms.entity.PurchaseRequestItem;
import org.computerspareparts.csms.repository.PartRepository;
import org.computerspareparts.csms.repository.SupplierRepository;
import org.computerspareparts.csms.repository.PurchaseRequestRepository;
import org.computerspareparts.csms.repository.PurchaseRequestItemRepository;
import org.computerspareparts.csms.repository.EmployeeRepository;
import org.computerspareparts.csms.entity.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class PurchaseRequestController {
    @Autowired
    private PartRepository partRepository;
    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private PurchaseRequestRepository purchaseRequestRepository;
    @Autowired
    private PurchaseRequestItemRepository purchaseRequestItemRepository;
    @Autowired
    private EmployeeRepository employeeRepository;

    @GetMapping("/manager/purchase-request")
    public String showPurchaseRequestForm(Model model) {
        List<Part> parts = partRepository.findAll();
        List<Supplier> suppliers = supplierRepository.findAll();
        model.addAttribute("parts", parts);
        model.addAttribute("suppliers", suppliers);
        return "purchase_request_form";
    }

    @PostMapping("/manager/purchase-request")
    public String submitPurchaseRequest(@RequestParam("supplierId") Integer supplierId,
                                        @RequestParam("partIds") List<Integer> partIds,
                                        @RequestParam("quantities") List<Integer> quantities,
                                        Principal principal) {
        Employee manager = employeeRepository.findByEmail(principal.getName()).orElseThrow();
        PurchaseRequest request = new PurchaseRequest();
        request.setManagerId(manager.getEmployeeId());
        request.setSupplierId(supplierId);
        request.setRequestDate(LocalDateTime.now());
        request.setStatus("Pending");
        BigDecimal totalAmount = BigDecimal.ZERO;
        request = purchaseRequestRepository.save(request);

        for (int i = 0; i < partIds.size(); i++) {
            Part part = partRepository.findById(partIds.get(i)).orElse(null);
            if (part != null) {
                PurchaseRequestItem item = new PurchaseRequestItem();
                item.setRequestId(request.getRequestId());
                item.setPartId(part.getPartId());
                item.setQuantity(quantities.get(i));
                item.setUnitPrice(part.getPrice());
                item.setStatus("Pending");
                purchaseRequestItemRepository.save(item);
                totalAmount = totalAmount.add(part.getPrice().multiply(BigDecimal.valueOf(quantities.get(i))));
            }
        }
        request.setTotalAmount(totalAmount);
        purchaseRequestRepository.save(request);
        return "redirect:/manager/home";
    }

    @GetMapping("/manager/purchase-requests")
    public String viewManagerPurchaseRequests(Model model, Principal principal) {
        String email = principal.getName();
        Employee manager = employeeRepository.findByEmail(email).orElse(null);
        if (manager == null) {
            model.addAttribute("error", "Manager not found.");
            return "manager_purchase_requests";
        }
        List<PurchaseRequest> requests = purchaseRequestRepository.findByManagerId(manager.getEmployeeId());
        model.addAttribute("requests", requests);
        return "manager_purchase_requests";
    }
}
