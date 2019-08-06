package com.github.alexthe666.iceandfire.compat.tinkers;

import slimeknights.mantle.client.book.BookTransformer;
import slimeknights.mantle.client.book.data.BookData;
import slimeknights.mantle.client.book.data.PageData;
import slimeknights.mantle.client.book.data.SectionData;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.book.content.ContentListing;
import slimeknights.tconstruct.library.book.content.ContentModifier;
import slimeknights.tconstruct.library.modifiers.IModifier;

public class IceAndFireBookTranformer extends BookTransformer {

    @Override
    public void transform(BookData book) {
        SectionData modifierSection = null;
        SectionData faModifierSection = null;
        for (SectionData section : book.sections) {
            if (section.name.equals("modifiers")) {
                modifierSection = section;
            }
            if (section.name.equals("iafmodifiers")) {
                faModifierSection = section;
            }
        }
        if(modifierSection != null && faModifierSection != null) {
            for (PageData page : faModifierSection.pages) {
                page.parent = modifierSection;
                modifierSection.pages.add(page);
            }
            PageData pageData = modifierSection.pages.get(0);
            if (pageData.content instanceof ContentListing) {
                for (PageData page : faModifierSection.pages) {
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
            faModifierSection.pages.clear();
            book.sections.remove(faModifierSection);
        }
    }
}