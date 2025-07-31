package tictactoe;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.ColorDescriptor;
import org.eclipse.swt.graphics.RGB;

public class TicTacToeGUI {
    protected Shell shell;
    private TicTacToe game;
    private Button[][] buttons;
    private Label statusLabel;
    private Font xFont;
    private Font oFont;
    private Color xColor;
    private Color oColor;
    private LocalResourceManager localResourceManager;

    public static void main(String[] args) {
        try {
            TicTacToeGUI window = new TicTacToeGUI();
            window.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void open() {
        Display display = Display.getDefault();
        createContents();
        shell.open();
        shell.layout();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        // Clean up resources
        xFont.dispose();
        oFont.dispose();
        xColor.dispose();
        oColor.dispose();
    }

    protected void createContents() {
        game = new TicTacToe();
        shell = new Shell();
        createResourceManager();
        shell.setBackground(localResourceManager.create(ColorDescriptor.createFrom(new RGB(255, 255, 255))));
        shell.setSize(450, 500); // Larger window
        shell.setText("Tic Tac Toe");
        
        // Use GridLayout with equal spacing
        GridLayout gridLayout = new GridLayout(3, true);
        gridLayout.horizontalSpacing = 5;
        gridLayout.verticalSpacing = 5;
        gridLayout.marginWidth = 20;
        gridLayout.marginHeight = 20;
        shell.setLayout(gridLayout);

        // Create custom fonts and colors
        xFont = new Font(shell.getDisplay(), new FontData("Arial", 48, SWT.BOLD));
        oFont = new Font(shell.getDisplay(), new FontData("Arial", 48, SWT.BOLD));
        xColor = new Color(shell.getDisplay(), 255, 128, 128); // Red for X
        oColor = new Color(shell.getDisplay(), 102, 179, 179); // Blue for O

        buttons = new Button[3][3];
        
        // Create game board buttons
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                buttons[row][col] = new Button(shell, SWT.PUSH);
                buttons[row][col].setText("");
                
                // Make buttons square and large
                GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
                gridData.widthHint = 100; // Minimum width
                gridData.heightHint = 100; // Minimum height
                buttons[row][col].setLayoutData(gridData);
                
                // Style the buttons
                buttons[row][col].setBackground(shell.getDisplay().getSystemColor(SWT.COLOR_WHITE));
                
                final int finalRow = row;
                final int finalCol = col;
                
                buttons[row][col].addSelectionListener(new SelectionAdapter() {
                    @Override
                    public void widgetSelected(SelectionEvent e) {
                        handleButtonClick(finalRow, finalCol);
                    }
                });
            }
        }
        
        // Status label - spans all 3 columns
        statusLabel = new Label(shell, SWT.CENTER);
        statusLabel.setForeground(localResourceManager.create(ColorDescriptor.createFrom(new RGB(61, 58, 90))));
        statusLabel.setText("Player X's turn");
        GridData statusGridData = new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1);
        statusGridData.verticalIndent = 15;
        statusLabel.setLayoutData(statusGridData);
        
        // Style the status label
        Font statusFont = new Font(shell.getDisplay(), new FontData("Arial", 14, SWT.BOLD));
        statusLabel.setFont(statusFont);
        
        // Reset button - spans all 3 columns
        Button resetButton = new Button(shell, SWT.PUSH);
        resetButton.setText("New Game");
        GridData resetGridData = new GridData(SWT.CENTER, SWT.CENTER, false, false, 3, 1);
        resetGridData.widthHint = 150;
        resetGridData.verticalIndent = 15;
        resetButton.setLayoutData(resetGridData);
        
        // Style the reset button
        resetButton.setBackground(localResourceManager.create(ColorDescriptor.createFrom(new RGB(162, 158, 199))));
        resetButton.setForeground(shell.getDisplay().getSystemColor(SWT.COLOR_WHITE));
        
        resetButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                resetGame();
            }
        });
    }
    private void createResourceManager() {
    	localResourceManager = new LocalResourceManager(JFaceResources.getResources(),shell);
    }
    
    private void handleButtonClick(int row, int col) {
        // Check if the move is valid
        if (game.getBoard()[row][col] == '-') {
            // Make the move
            game.getBoard()[row][col] = game.getCurrentPlayer();
            
            // Update button appearance
            Button button = buttons[row][col];
            button.setText(String.valueOf(game.getCurrentPlayer()));
            
            // Style based on player
            if (game.getCurrentPlayer() == 'X') {
                button.setFont(xFont);
                button.setForeground(xColor);
            } else {
                button.setFont(oFont);
                button.setForeground(oColor);
            }
            
            // Check for win or draw
            if (game.checkForWin()) {
                statusLabel.setText("Player " + game.getCurrentPlayer() + " wins!");
                disableAllButtons();
                highlightWinningCells();
            } else if (game.isBoardFull()) {
                statusLabel.setText("The game is a draw!");
            } else {
                // Switch players
                game.changePlayer();
                statusLabel.setText("Player " + game.getCurrentPlayer() + "'s turn");
            }
        }
    }
    
    private void disableAllButtons() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                buttons[row][col].setEnabled(false);
            }
        }
    }
    
    private void highlightWinningCells() {
        // This would need to be implemented based on your win detection logic
        // For now, we'll just make all cells slightly darker
        Color winColor = new Color(shell.getDisplay(), 220, 220, 220);
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                buttons[row][col].setBackground(winColor);
            }
        }
        winColor.dispose();
    }
    
    private void resetGame() {
        game = new TicTacToe();
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                buttons[row][col].setText("");
                buttons[row][col].setEnabled(true);
                buttons[row][col].setBackground(shell.getDisplay().getSystemColor(SWT.COLOR_WHITE));
            }
        }
        statusLabel.setText("Player X's turn");
    }
}