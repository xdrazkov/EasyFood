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
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class BatchPdfExporter implements BatchExporter {

    private static final Format FORMAT = new Format("PDF", List.of("pdf"));

    @Override
    public Format getFormat() {
        return FORMAT;
    }

    @Override
    public void exportBatch(Batch batch, String filePath) {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                URL url = BatchPdfExporter.class.getResource("Helvetica.ttf");
                if (url == null) {
                    throw new IllegalArgumentException("PDType0Font resource not found on classpath helvetica.ttf ");
                }
                PDType0Font helveticaFont = PDType0Font.load(document, url.openStream());
                contentStream.setFont(helveticaFont, 12);

                float margin = 50; // Adjust the margin as needed
                float yStart = page.getMediaBox().getHeight() - margin;
                float yPosition = yStart;
                float lineHeight = 15f; // Adjust the line height as needed

                // Write batch information to the PDF content stream
                contentStream.beginText();
                contentStream.newLineAtOffset(margin, yPosition);

                for (Recipe recipe : batch.recipes()) {
                    yPosition -= lineHeight;
                    contentStream.newLineAtOffset(0, -lineHeight);
                    contentStream.showText("Title: " + recipe.getTitle());

                    yPosition -= lineHeight;
                    contentStream.newLineAtOffset(0, -lineHeight);
                    contentStream.showText("Description: " + recipe.getDescription());

                    yPosition -= lineHeight;
                    contentStream.newLineAtOffset(0, -lineHeight);
                    contentStream.showText("Portion Count: " + recipe.getPortionCount());

                    yPosition -= lineHeight;
                    contentStream.newLineAtOffset(0, -lineHeight);
                    contentStream.showText("Instructions: " + recipe.getInstructions());

                    yPosition -= lineHeight;
                    contentStream.newLineAtOffset(0, -lineHeight);
                    contentStream.showText("Time to Prepare: " + recipe.getTimeToPrepare() + " minutes");

                    yPosition -= lineHeight;
                    contentStream.newLineAtOffset(0, -lineHeight);
                    contentStream.showText("Nutritional Value: " + recipe.getNutritionalValue());

                    yPosition -= lineHeight;
                    contentStream.newLineAtOffset(0, -lineHeight);
                    contentStream.showText("Category: " + recipe.getCategory().getName());

                    // Ingredients
                    yPosition -= lineHeight;
                    contentStream.newLineAtOffset(0, -lineHeight);
                    contentStream.showText("Ingredients:");

                    contentStream.newLineAtOffset(20, 0);

                    for (Map.Entry<Ingredient, Pair<Unit, Integer>> entry : recipe.getIngredients().entrySet()) {
                        Ingredient ingredient = entry.getKey();
                        Pair<Unit, Integer> ingredientDetails = entry.getValue();

                        contentStream.newLineAtOffset(0, -lineHeight);

                        contentStream.newLine();
                        contentStream.showText(" - " + ingredient.getName() + ": " +
                                ingredientDetails.getRight() + " " + ingredientDetails.getLeft().getName());
                    }

                    contentStream.newLineAtOffset(-20, -lineHeight);
                    contentStream.newLine();  // Add a newline after each recipe
                }

                contentStream.endText();
            }

            document.save(filePath);
        } catch (IOException e) {
            throw new DataManipulationException("Export to PDF not successful", e);
        }
    }

}

