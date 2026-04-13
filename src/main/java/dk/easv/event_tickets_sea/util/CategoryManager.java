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
    public ObservableList<Category> getCategories() {
        return categoryDAO.getAllCategories();
    }

    /**
     * Přidá novou kategorii
     */
    public boolean addCategory(String categoryName, String description) {
        return categoryDAO.addCategory(categoryName, description);
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
        return categoryDAO.updateCategory(category.getCategoryId(), category.getCategoryName(), category.getDescription());
    }
}

