package Utils;

import Models.*;

import java.util.HashSet;
import java.util.SortedMap;

public class ModelStructure {
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

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

    public ModelStructure(HashSet<Expense> expenses, HashSet<Source> sources, HashSet<Tag> tags) {
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

    public void deleteTag(Tag tag) {
        tags.remove(tag);
        tagNames.remove(tag.getName());
    }
    public void deleteCategory(Category category) {
        categories.remove(category);
        System.out.println("czy usnieęto nazwe kategori? :"+categoryNames.remove(category.getName()));
    }
    public void deleteExpense(Expense expense) {
        expenses.remove(expense);
    }
    public void deleteSource(Source source) {
        sources.remove(source);
        sourceNames.remove(source.getName());
    }
    public void deleteName(Name name) {
        names.remove(name);
        expenseNames.remove(name.getName());
    }
    public void deleteUnit(Unit unit) {
        units.remove(unit);
        unitShortcuts.remove(unit.getShortcut());
        unitNames.remove(unit.getName());
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
