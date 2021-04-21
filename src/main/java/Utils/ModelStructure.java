package Utils;

import Dao.DaoContainer;
import Models.*;

import java.sql.Connection;
import java.util.HashSet;
import java.util.SortedMap;

public class ModelStructure {
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    private DaoContainer daoContainer;

    private HashSet<Expense> expenses;
    private HashSet<Source> sources;
    private HashSet<Name> names = new HashSet<>();
    private HashSet<Category> categories = new HashSet<>();
    private HashSet<Unit> units = new HashSet<>();
    private HashSet<Tag> tags = new HashSet<>();

    private HashSet<String> sourceNames = new HashSet<>();
    private HashSet<String> expenseNames = new HashSet<>();
    private HashSet<String> unitNames = new HashSet<>();
    private HashSet<String> unitShortcuts = new HashSet<>();
    private HashSet<String> categoryNames = new HashSet<>();
    private HashSet<String> tagNames = new HashSet<>();

    public DaoContainer getDaoContainer() {
        return daoContainer;
    }

    public ModelStructure(DaoContainer daoContainer, HashSet<Expense> expenses, HashSet<Source> sources, HashSet<Tag> tags) {
        this.daoContainer = daoContainer;
        for (Tag tag : tags
        ) {
            tagNames.add(tag.getName());
        }
        this.tags = tags;
        for (Source source : sources
        ) {
            sourceNames.add(source.getName());
        }
        this.sources = sources;
        for (Expense expense : expenses
        ) {
            Name name = expense.getName();
            names.add(name);
            expenseNames.add(name.getName());
            Category category = name.getCategory();
            categoryNames.add(category.getName());
            categories.add(category);
            Unit unit = expense.getUnit();
            units.add(unit);
            unitNames.add(unit.getName());
            unitShortcuts.add(unit.getShortcut());
        }
        this.expenses = expenses;
    }

    public HashSet<Tag> getTags() {
        return tags;
    }

    public HashSet<String> getTagNames() {
        return tagNames;
    }

    public HashSet<String> getSourceNames() {
        return sourceNames;
    }

    public HashSet<Expense> getExpenses() {
        return expenses;
    }

    public HashSet<Source> getSources() {
        return sources;
    }

    public HashSet<Name> getNames() {
        return names;
    }

    public HashSet<Category> getCategories() {
        return categories;
    }

    public HashSet<Unit> getUnits() {
        return units;
    }

    public HashSet<String> getExpenseNames() {
        return expenseNames;
    }

    public HashSet<String> getUnitNames() {
        return unitNames;
    }

    public HashSet<String> getUnitShortcuts() {
        return unitShortcuts;
    }

    public HashSet<String> getCategoryNames() {
        return categoryNames;
    }

    public HashSet<String> getNamesAndShortcuts() {
        HashSet<String> all = new HashSet<>();
        all.addAll(getUnitNames());
        all.addAll(getUnitShortcuts());
        return all;
    }
    public void addNewTag(Tag tag){
        tags.add(tag);
        tagNames.add(tag.getName());
    }
    public void addNewName(Name name) {
        names.add(name);
        expenseNames.add(name.getName());
    }

    public void addNewUnit(Unit unit) {
        units.add(unit);
        unitNames.add(unit.getName());
        unitShortcuts.add(unit.getShortcut());
    }

    public void addNewCategory(Category category) {
        categories.add(category);
        categoryNames.add(category.getName());
    }
    public void addNewSource(Source source) {
        sources.add(source);
        sourceNames.add(source.getName());
    }

    public boolean deleteTag(Tag tag, boolean permanently) {
        boolean delete = true;
        if(permanently){
            delete = daoContainer.getTagDao().delete(tag);
        }
        if(delete){
            delete = tags.remove(tag);
            tagNames.remove(tag.getName());
        }
        return delete;
    }
    public boolean deleteCategory(Category category, boolean permanently) {
        boolean delete = true;
        if(permanently){
            delete = daoContainer.getCategoryDao().delete(category);
        }
        if(delete){
            delete = categories.remove(category);
            categoryNames.remove(category.getName());
        }
        return delete;
    }
    public boolean deleteExpense(Expense expense, boolean permanently) {
        boolean delete = true;
        if(permanently){
            delete = daoContainer.getExpenseDao().delete(expense);
        }
        if(delete){
            delete = expenses.remove(expense);
        }
        return delete;
    }
    public boolean deleteSource(Source source, boolean permanently) {
        boolean delete = true;
        if(permanently){
            delete = daoContainer.getSourceDao().delete(source);
        }
        if(delete){
            delete = sources.remove(source);
            sourceNames.remove(source.getName());
        }
        return delete;

    }
    public boolean deleteName(Name name, boolean permanently) {
        boolean delete = true;
        if(permanently){
            delete = daoContainer.getNameDao().delete(name);
        }
        if(delete){
            delete = names.remove(name);
            expenseNames.remove(name.getName());
        }
        return delete;

    }
    public boolean deleteUnit(Unit unit, boolean permanently) {
        boolean delete = true;
        if(permanently){
            delete = daoContainer.getUnitDao().delete(unit);
        }
        if(delete){
            delete = units.remove(unit);
            unitShortcuts.remove(unit.getShortcut());
            unitNames.remove(unit.getName());
        }
        return delete;
    }

    public void replaceNewSource(Source source) {
        source.setId(-1);
        for (Source item : sources
        ) {
            if (item.getId() == -1) {
                String name = item.getName();
                sources.remove(item);
                sourceNames.remove(name);
                break;
            }
        }
        sources.add(source);
        sourceNames.add(source.getName());
    }

    public void clearNames(){
        boolean deleted = names.removeIf(item -> (item.getId()<0));
        if(deleted){
            expenseNames.clear();
            names.forEach(name -> {
                expenseNames.add(name.getName());
            });

        }

    }
    public void clearCategories(){
        boolean deleted = categories.removeIf(item -> (item.getId()<0));
        if(deleted){
            categoryNames.clear();
            categories.forEach(name -> {
                categoryNames.add(name.getName());
            });

        }
    }
    public void clearUnits(){
        boolean deleted = units.removeIf(item -> (item.getId()<0));
        if(deleted){
            unitShortcuts.clear();
            unitNames.clear();
            units.forEach(name -> {
                unitNames.add(name.getName());
            });

        }
    }
}
