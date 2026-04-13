package dk.easv.event_tickets_sea.util;

import dk.easv.event_tickets_sea.db.CategoryDAO;
import dk.easv.event_tickets_sea.model.Category;
import javafx.collections.ObservableList;

public class CategoryManager {
    private static CategoryManager instance;
    private final CategoryDAO categoryDAO;

    private CategoryManager() {
        this.categoryDAO = CategoryDAO.getInstance();
    }

    public static CategoryManager getInstance() {
        if (instance == null) {
            instance = new CategoryManager();
        }
        return instance;
    }

    /**
     * Získá všechny kategorie z databáze
     */
    public ObservableList<Category> getCategories(String eventName) {
        return categoryDAO.getAllCategories(eventName);
    }

    /**
     * Přidá novou kategorii
     */
    public boolean addCategory(String eventName, String categoryName) {
        return categoryDAO.addCategory(eventName, categoryName);
    }

    /**
     * Odstraní kategorii
     */
    public void removeCategory(Category category) {
        categoryDAO.deleteCategory(category.getCategoryId());
    }

    /**
     * Aktualizuje kategorii
     */
    public boolean updateCategory(Category category) {
        return categoryDAO.updateCategory(category.getCategoryId(), category.getCategoryName());
    }
}

