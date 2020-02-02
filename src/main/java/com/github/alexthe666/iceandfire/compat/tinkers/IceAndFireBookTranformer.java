package com.github.alexthe666.iceandfire.compat.tinkers;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.google.common.collect.Lists;
import slimeknights.mantle.client.book.BookTransformer;
import slimeknights.mantle.client.book.data.BookData;
import slimeknights.mantle.client.book.data.PageData;
import slimeknights.mantle.client.book.data.SectionData;
import slimeknights.mantle.client.book.data.content.PageContent;
import slimeknights.mantle.client.book.data.element.ImageData;
import slimeknights.mantle.client.gui.book.element.ElementImage;
import slimeknights.mantle.client.gui.book.element.ElementItem;
import slimeknights.mantle.client.gui.book.element.SizedBookElement;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.book.content.*;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.materials.MaterialTypes;
import slimeknights.tconstruct.library.modifiers.IModifier;

import java.util.*;
import java.util.stream.Collectors;

import static com.github.alexthe666.iceandfire.compat.tinkers.TinkersCompat.MATERIAL_WEEZER;

public class IceAndFireBookTranformer extends BookTransformer {

    public static List<ContentPageIconList> getPagesNeededForItemCount(int addLoc, int count, SectionData data, String title) {
        List<ContentPageIconList> listPages = Lists.newArrayList();
        while (count > 0) {
            ContentPageIconList overview = new ContentPageIconList();
            PageData page = new PageData(true);
            page.source = data.source;
            page.parent = data;
            page.content = overview;
            page.load();
            data.pages.add(addLoc, page);
            overview.title = title;
            listPages.add(overview);
            count -= overview.getMaxIconCount();
        }
        // ensure same size for all
        if (listPages.size() > 1) {
            listPages.forEach(page -> page.maxScale = 1f);
        }
        return listPages;
    }

    @Override
    public void transform(BookData book) {
        SectionData modifierSection = null;
        SectionData iafModifierSection = null;
        SectionData materialsSection = null;
        SectionData bowMaterialsSection = null;
        for (SectionData section : book.sections) {
            if (section.name.equals("modifiers")) {
                modifierSection = section;
            }
            if (section.name.equals("iafmodifiers")) {
                iafModifierSection = section;
            }
            if (section.name.equals("materials")) {
                materialsSection = section;
            }
            if (section.name.equals("bowmaterials")) {
                bowMaterialsSection = section;
            }
        }
        if (modifierSection != null && iafModifierSection != null) {
            for (PageData page : iafModifierSection.pages) {
                page.parent = modifierSection;
                modifierSection.pages.add(page);
            }
            PageData pageData = modifierSection.pages.get(0);
            if (pageData.content instanceof ContentListing) {
                for (PageData page : iafModifierSection.pages) {
                    page.parent = modifierSection;
                    if (page.content instanceof ContentModifier) {
                        IModifier modifier = TinkerRegistry.getModifier(((ContentModifier) page.content).modifierName);
                        if (modifier != null) {
                            page.name = "iaf_" + modifier.getIdentifier();
                            ((ContentListing) pageData.content).addEntry(modifier.getLocalizedName(), page);
                        }
                    }
                }
            }
            iafModifierSection.pages.clear();
            book.sections.remove(iafModifierSection);
        }
        if (IceAndFire.CONFIG.weezerTinkers) {
            try {
                if (materialsSection != null) {
                    Iterator<PageData> itr = materialsSection.pages.iterator();
                    ContentPageIconList previousContents = null;
                    Map<String, PageData> previousMaterialPages = new HashMap<>();
                    while (itr.hasNext()) {
                        PageData data = itr.next();
                        if (data.type != null && data.type.equalsIgnoreCase("toolmaterial")) {
                            previousMaterialPages.put(data.name, data);
                        }
                        if (data.name.equalsIgnoreCase(TinkersCompat.MATERIAL_WEEZER.identifier)) {
                            itr.remove();
                        }

                        if (data.content instanceof ContentPageIconList) {
                            previousContents = (ContentPageIconList) data.content;
                            itr.remove();
                        }
                    }
                    if (previousContents != null) {
                        List<Material> materialList = TinkerRegistry.getAllMaterials().stream()
                                .filter(m -> !m.isHidden())
                                .filter(this::isValidMaterial)
                                .filter(Material::hasItems)
                                .collect(Collectors.toList());
                        List<ContentPageIconList> listPages = getPagesNeededForItemCount(0, materialList.size(), materialsSection, book.translate("materials"));
                        ListIterator<ContentPageIconList> iter = listPages.listIterator();
                        ContentPageIconList overview = iter.next();
                        for (Material material : materialList) {
                            PageData page = previousMaterialPages.get(material.identifier);
                            SizedBookElement icon;
                            if (material.getRepresentativeItem() != null) {
                                icon = new ElementItem(0, 0, 1f, material.getRepresentativeItem());
                            } else {
                                icon = new ElementImage(ImageData.MISSING);
                            }
                            if (page != null) {
                                while (!overview.addLink(icon, material.getLocalizedNameColored(), page)) {
                                    overview = iter.next();
                                }
                            }
                        }

                    }
                }

                if (bowMaterialsSection != null) {
                    Iterator<PageData> itr = bowMaterialsSection.pages.iterator();
                    int contentLoc = 0;
                    ContentPageIconList previousContents = null;
                    Map<String, PageData> previousMaterialPages = new HashMap<>();
                    while (itr.hasNext()) {
                        PageData data = itr.next();
                        System.out.println(data.name + " " + data.type + " " + data.content);
                        if (data.content instanceof ContentPageIconList && previousContents == null) {
                            previousContents = (ContentPageIconList) data.content;
                            contentLoc = bowMaterialsSection.pages.indexOf(data);
                            itr.remove();
                        } else if (data.type != null && data.type.equalsIgnoreCase("single_stat_material")) {
                            previousMaterialPages.put(data.name, data);
                        }
                        if (data.name.contains(MATERIAL_WEEZER.identifier)) {
                            itr.remove();
                        }

                    }
                    if (previousContents != null) {
                        List<Material> materialList = TinkerRegistry.getAllMaterials().stream()
                                .filter(m -> !m.isHidden())
                                .filter(Material::hasItems)
                                .filter(material -> material.hasStats(MaterialTypes.BOW))
                                .collect(Collectors.toList());
                        List<ContentPageIconList> listPages = getPagesNeededForItemCount(contentLoc, materialList.size(), bowMaterialsSection, book.translate("materials"));
                        listPages.forEach(p -> p.maxScale = 1f);
                        ListIterator<ContentPageIconList> iter = listPages.listIterator();
                        ContentPageIconList overview = iter.next();
                        for (List<Material> materials : Lists.partition(materialList, 3)) {
                            ContentSingleStatMultMaterial content = new ContentSingleStatMultMaterial(materials, MaterialTypes.BOW);
                            String id = MaterialTypes.BOW + "_" + materials.stream().map(Material::getIdentifier).collect(Collectors.joining("_"));
                            PageData page = previousMaterialPages.get(id);
                            for (Material material : materials) {
                                SizedBookElement icon;
                                if (!material.identifier.equalsIgnoreCase(MATERIAL_WEEZER.identifier)) {
                                    if (material.getRepresentativeItem() != null) {
                                        icon = new ElementItem(0, 0, 1f, material.getRepresentativeItem());
                                    } else {
                                        icon = new ElementImage(ImageData.MISSING);
                                    }
                                    if (page != null) {
                                        while (!overview.addLink(icon, material.getLocalizedNameColored(), page)) {
                                            overview = iter.next();
                                        }
                                    }
                                }
                            }
                        }

                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    protected PageData addPage(SectionData data, String name, String type, PageContent content) {
        PageData page = new PageData(true);
        page.source = data.source;
        page.parent = data;
        page.name = name;
        page.type = type;
        page.content = content;
        page.load();

        data.pages.add(page);

        return page;
    }

    protected boolean isValidMaterial(Material material) {
        return (material.hasStats(MaterialTypes.HEAD) || material.hasStats(MaterialTypes.HEAD) || material.hasStats(MaterialTypes.HEAD)) && !material.identifier.equalsIgnoreCase(MATERIAL_WEEZER.identifier);
    }

    protected PageContent getPageContent(Material material) {
        return new ContentMaterial(material);
    }
}