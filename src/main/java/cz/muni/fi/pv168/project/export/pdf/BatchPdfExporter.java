package cz.muni.fi.pv168.project.export.pdf;

import cz.muni.fi.pv168.project.model.Ingredient;
import cz.muni.fi.pv168.project.model.Recipe;
import cz.muni.fi.pv168.project.model.Unit;
import cz.muni.fi.pv168.project.service.export.DataManipulationException;
import cz.muni.fi.pv168.project.service.export.batch.Batch;
import cz.muni.fi.pv168.project.service.export.batch.BatchExporter;
import cz.muni.fi.pv168.project.service.export.format.Format;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class BatchPdfExporter implements BatchExporter {

    private static final Format FORMAT = new Format("PDF", List.of("pdf"));
    private static final float MARGIN = 50;
    private static final float LINE_HEIGHT = 19f;
    private static final PDFont FONT = PDType1Font.HELVETICA;
    private static final PDFont FONT_BOLD = PDType1Font.HELVETICA_BOLD;
    private PDPageContentStream contentStream;
    private float yPosition;
    private float xPosition;

    @Override
    public Format getFormat() {
        return FORMAT;
    }

    @Override
    public void exportBatch(Batch batch, String filePath) {
        this.contentStream = null;
        try (PDDocument document = new PDDocument()) {
            int textFontSize = 15;
            int leftTab = 20;

            for (Recipe recipe : batch.recipes()) {
                writeLine(document, "Title:", FONT_BOLD, textFontSize);
                writeLine(document, recipe.getTitle(), FONT, textFontSize, leftTab);
                writeLine(document, "Description:", FONT_BOLD, textFontSize);
                writeLine(document, recipe.getDescription(), FONT, textFontSize, leftTab);
                writeLine(document, "Portion Count:", FONT_BOLD, textFontSize);
                writeLine(document, recipe.getPortionCount() + "", FONT, textFontSize, leftTab);
                writeLine(document, "Instructions:", FONT_BOLD, textFontSize);
                writeLine(document, recipe.getInstructions(), FONT, textFontSize, leftTab);
                writeLine(document, "Time to Prepare:", FONT_BOLD, textFontSize);
                writeLine(document, recipe.getTimeToPrepare() + " minutes", FONT, textFontSize, leftTab);
                writeLine(document, "Nutritional Value:", FONT_BOLD, textFontSize);
                writeLine(document, recipe.getNutritionalValue() + " kcal", FONT, textFontSize, leftTab);
                writeLine(document, "Category: ", FONT_BOLD, textFontSize);
                writeLine(document,  recipe.getCategory().getName(), FONT, textFontSize, leftTab);

                writeLine(document, "Ingredients:", FONT_BOLD, textFontSize);
                for (Map.Entry<Ingredient, Pair<Unit, Integer>> entry : recipe.getIngredients().entrySet()) {
                    Ingredient ingredient = entry.getKey();
                    Pair<Unit, Integer> ingredientDetails = entry.getValue();

                    writeLine(document, " - " + ingredient.getName() + ": " +
                            ingredientDetails.getRight() + " " + ingredientDetails.getLeft().getName(), FONT, textFontSize, 20);
                }

                writeLine(document, "", FONT, textFontSize);
                writeLine(document, "", FONT, textFontSize);
            }

            contentStream.endText();
            contentStream.close();
            document.save(filePath);
        } catch (IOException e) {
            throw new DataManipulationException("Export to PDF not successful", e);
        }
    }

    private static float getStringWidth(PDFont font, String text, float fontSize) throws IOException {
        return font.getStringWidth(text) * fontSize / 1000f;
    }

    private void writeLine(PDDocument document, String text, PDFont font, int fontSize) throws IOException {
        writeLine(document, text,  font, fontSize, 0);
    }

    private void writeLine(PDDocument document, String text, PDFont font, int fontSize, float leftTab) throws IOException {
        var page = getCurrentPage(document);
        if (page == null) {
            page = new PDPage();
            document.addPage(page);
            initYPosition(page);
            initXPosition();
        }

        float usablePageWidth = page.getMediaBox().getWidth() - 2 * MARGIN - leftTab;

        String[] words = text.split(" ");
        StringBuilder stringBuilder = new StringBuilder();
        for (String word : words) {
            if (isLeftOverFlow(stringBuilder + word, font, fontSize, usablePageWidth)) {
                writeOneLine(document, stringBuilder.toString(),  font, fontSize, leftTab);
                stringBuilder.setLength(0);
            }
            stringBuilder.append(word);
            stringBuilder.append(" ");
        }

        writeOneLine(document, stringBuilder.toString(),  font, fontSize, leftTab);
    }


    /** cannot be called alone **/
    private void writeOneLine(PDDocument document, String text, PDFont font, int fontSize, float leftTab) throws IOException{
        var page = getCurrentPage(document);
        if (page == null) {
            page = new PDPage();
            document.addPage(page);
            initYPosition(page);
            initXPosition();
        }

        if (contentStream == null) {
            initContentStream(document, page);
        }

        yPosition -= LINE_HEIGHT;
        if (isBottomOverflow(yPosition)) {
            contentStream.endText();
            contentStream.close();
            page = new PDPage();
            document.addPage(page);

            initXPosition();
            initYPosition(page);
            initContentStream(document, page);
        }

        contentStream.setFont(font, fontSize);
        contentStream.newLineAtOffset(leftTab, -LINE_HEIGHT);
        contentStream.showText(text);
        contentStream.newLineAtOffset(-leftTab, 0);
    }

    private void initXPosition() {
        xPosition = MARGIN;
    }
    private void initYPosition(PDPage page) {
        yPosition = page.getMediaBox().getHeight() - MARGIN;
    }
    private void initContentStream(PDDocument document,  PDPage page) throws IOException {
        contentStream = new PDPageContentStream(document, page);
        contentStream.beginText();
        contentStream.newLineAtOffset(MARGIN, yPosition);
    }

    private static boolean isBottomOverflow(float yPosition) {
        return yPosition < MARGIN;
    }

    private static boolean isLeftOverFlow(String text, PDFont font, float fontSize, float usablePageWidth) throws IOException{
        return getStringWidth(font, text, fontSize) > usablePageWidth;
    }

    private static PDPage getCurrentPage(PDDocument document) {
        int pagesCount = document.getNumberOfPages();
        return (pagesCount != 0)? document.getPage(pagesCount - 1): null;
    }
}

