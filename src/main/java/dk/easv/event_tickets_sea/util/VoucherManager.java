package dk.easv.event_tickets_sea.util;

import dk.easv.event_tickets_sea.db.VoucherDAO;
import dk.easv.event_tickets_sea.model.Voucher;
import javafx.collections.ObservableList;

public class VoucherManager {
    private static VoucherManager instance;
    private final VoucherDAO voucherDAO;

    private VoucherManager() {
        this.voucherDAO = VoucherDAO.getInstance();
    }

    public static VoucherManager getInstance() {
        if (instance == null) {
            instance = new VoucherManager();
        }
        return instance;
    }

    public ObservableList<String> createVouchers(String description, String eventName, String createdByUsername, int quantity) {
        return voucherDAO.createVouchers(description, eventName, createdByUsername, quantity);
    }

    public ObservableList<Voucher> getAllVouchers() {
        return voucherDAO.getAllVouchers();
    }
}
