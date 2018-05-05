package org.ql.audioeditor.view.panel;

import org.ql.audioeditor.view.core.button.OptionButton;
import org.ql.audioeditor.view.core.panel.BasicPanel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;

/**
 * Menu.
 */
public final class OptionPanel extends BasicPanel {
    private final OptionButton mainMenuOptionButton;
    private final OptionButton openFileOptionButton;
    private final OptionButton viewSongsOptionButton;
    private final OptionButton showDataButton;
    private final OptionButton changePitchOptionButton;
    private final OptionButton cutOptionButton;
    private final OptionButton fftOptionButton;
    private final OptionButton chromaOptionButton;
    private final OptionButton analyzeOptionButton;
    private GridBagConstraints c;

    /**
     * Constructor.
     *
     * @param mm  MainMenuOptionButton listener
     * @param of  OpenFileOptionButton listener
     * @param vs  ViewSongsOptionButton listener
     * @param sd  ShowDataButton listener
     * @param cp  ChangePitchOptionButton listener
     * @param cf  CutOptionButton listener
     * @param fft FftOptionButton listener
     * @param cg  ChromaOptionButton listener
     * @param as  AnalyzeOptionButton listener
     */
    public OptionPanel(ActionListener mm, ActionListener of, ActionListener vs,
        ActionListener sd, ActionListener cp,
        ActionListener cf, ActionListener fft, ActionListener cg,
        ActionListener as) {
        mainMenuOptionButton = new OptionButton("Main menu", mm);
        openFileOptionButton = new OptionButton("Open file", of);
        viewSongsOptionButton = new OptionButton("View favorite songs", vs);
        showDataButton = new OptionButton("View song data", sd);
        changePitchOptionButton = new OptionButton("Change pitch", cp);
        cutOptionButton = new OptionButton("Cut file", cf);
        fftOptionButton = new OptionButton("View spectrogram", fft);
        chromaOptionButton = new OptionButton("View chromagram", cg);
        analyzeOptionButton = new OptionButton("Analyze song", as);
        setStyle();
        addPanels();
    }

    /**
     * Shows option buttons.
     *
     * @param l Logical value (true if should show)
     */
    public void showOptions(boolean l) {
        showDataButton.setVisible(l);
        changePitchOptionButton.setVisible(l);
        cutOptionButton.setVisible(l);
        fftOptionButton.setVisible(l);
        chromaOptionButton.setVisible(l);
        analyzeOptionButton.setVisible(l);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setStyle() {
        setOpaque(false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void addPanels() {
        setLayout(new GridBagLayout());
        c = new GridBagConstraints();
        c.gridy = GridBagConstraints.RELATIVE;
        c.gridx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.anchor = GridBagConstraints.NORTH;
        add(mainMenuOptionButton, c);
        add(openFileOptionButton, c);
        add(viewSongsOptionButton, c);
        add(showDataButton, c);
        add(changePitchOptionButton, c);
        add(cutOptionButton, c);
        add(fftOptionButton, c);
        add(chromaOptionButton, c);
        add(analyzeOptionButton, c);
    }
}
