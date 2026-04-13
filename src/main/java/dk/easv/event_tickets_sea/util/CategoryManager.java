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

    /** Get all categories for a specific event */
    public ObservableList<Category> getCategories(String eventName) {
        return categoryDAO.getAllCategories(eventName);
    }

    /** Add a new category with price and quantity */
    public boolean addCategory(String eventName, String categoryName, String description, double price, int quantity) {
        return categoryDAO.addCategory(eventName, categoryName, description, price, quantity);
    }

    /** Update an existing category */
    public boolean updateCategory(int categoryId, String categoryName, String description, double price, int quantity) {
        return categoryDAO.updateCategory(categoryId, categoryName, description, price, quantity);
    }

    /** Remove a category */
    public void removeCategory(Category category) {
        categoryDAO.deleteCategory(category.getCategoryId());
    }
}
