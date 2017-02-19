package dad.calendario;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.Semaphore;

import javax.print.attribute.standard.DialogTypeSelection;

import dad.calendario.MonthCalendar;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SetProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleSetProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class CalendarioController extends BorderPane  implements Initializable{

	private Task<Void> dateChekTask;
	
	private IntegerProperty year;
	private BooleanProperty comprobarDia;
	private ObjectProperty<LocalDate> today;
	
	private SetProperty<LocalDate> specialDays;
	
	private ObjectProperty<LocalDate> selectedDay;
	
	@FXML
	GridPane mesesPane;
	@FXML
	Label anyoLabel;
	
	@FXML
	private GridPane yearGrid;
	
	@FXML
	private List<MonthCalendar> mothList;

	public CalendarioController() {
		selectedDay= new SimpleObjectProperty<>(this,"selectedDay",LocalDate.now());
		
		
		year = new SimpleIntegerProperty(this, "year", LocalDate.now().getYear());
		specialDays= new SimpleSetProperty<>(this,"specialDays",FXCollections.observableSet());
		specialDays.addListener((obs,oldV,newV)->onSpecialDaysChange());
		
		today = new SimpleObjectProperty<>(this, "today", LocalDate.now());
		CalendarService service =new CalendarService();
		service.start();
		//TODO
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("CalendarioView.fxml"));
			loader.setController(this);
			loader.setRoot(this);
			loader.load();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		anyoLabel.textProperty().bind(year.asString());
		
		selectedDay.addListener((obs,oldV,newV)-> {
			if (newV.getYear()>year.get())
				while(newV.getYear()!=year.get())
				incrementarYear();
			else
				while(newV.getYear()!=year.get())
				decrementarYear();
			
		});



	}
	
	private void onSpecialDaysChange() {
		
		mothList.forEach(mothC->{
			mothC.getDaysLabel().forEach(r->r.getStyleClass().remove("specialDay"));
		});
			
			
		for (LocalDate labelDate : specialDays) {
			if (labelDate.getYear()==year.get()) {
			MonthCalendar targetMes = mothList.get(labelDate.getMonthValue()-1);
			Label dayLabel = targetMes.getLabelByDayOfMoth(labelDate.getDayOfMonth());
			dayLabel.getStyleClass().add("specialDay");
			}
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		for (int i = 0; i < mothList.size(); i++) {
			mothList.get(i).initDate(i+1, year.get());
			mothList.get(i).yearProperty().bind(year);
			mothList.get(i).todayProperty().bind(today);
			mesesPane.add(mothList.get(i).getView(),  i % 3, i / 3);
			mothList.get(i).diaPulsadoEventProperty().addListener((obs,oldV,newV)->onDayClickedAction(newV));

		}
		
		
	}

	private void onDayClickedAction(LocalDate newV) {
		if (newV!=null) {
			selectedDay.set(newV);
		}
	}

	public synchronized void incrementarYear() {
		
		
		
		double x=300;
		SnapshotParameters sp = new SnapshotParameters();
		sp.setFill(Color.TRANSPARENT);
		WritableImage image = anyoLabel.snapshot(sp, null);
		ImageView oldYear = new ImageView(image);
		oldYear.setMouseTransparent(true);

		year.set(year.get() + 1);
		
		
		getChildren().add(oldYear);
		oldYear.setLayoutX(anyoLabel.getLayoutX());
		oldYear.setLayoutY(anyoLabel.getLayoutY());
		
		TranslateTransition out = new TranslateTransition(Duration.millis(500),oldYear);
		out.setFromX(0);
		out.setToX(x);
		
		FadeTransition out2 = new FadeTransition(Duration.millis(500),oldYear);
		out2.setFromValue(1);
		out2.setToValue(0);
	
		
		
		TranslateTransition in = new TranslateTransition(Duration.millis(500),anyoLabel);
		in.setFromX(-x);
		in.setToX(0);
		

		
		FadeTransition in2 = new FadeTransition(Duration.millis(500),anyoLabel);
		in2.setFromValue(0);
		in2.setToValue(1);
		
		ParallelTransition pr = new ParallelTransition(out,out2,in,in2);
		pr.setOnFinished(e->getChildren().remove(oldYear));
		pr.play();
		onSpecialDaysChange();
	}

	
	public synchronized void decrementarYear() {
		double x=300;
		SnapshotParameters sp = new SnapshotParameters();
		sp.setFill(Color.TRANSPARENT);
		WritableImage image = anyoLabel.snapshot(sp, null);
		ImageView oldYear = new ImageView(image);
		oldYear.setMouseTransparent(true);
		year.set(year.get() - 1);
		
		getChildren().add(oldYear);
		oldYear.setLayoutX(anyoLabel.getLayoutX());
		oldYear.setLayoutY(anyoLabel.getLayoutY());
		
		TranslateTransition out = new TranslateTransition(Duration.millis(500),oldYear);
		out.setFromX(0);
		out.setToX(-x);
		FadeTransition out2 = new FadeTransition(Duration.millis(500),oldYear);
		out2.setFromValue(1);
		out2.setToValue(0);
		
		
		TranslateTransition in = new TranslateTransition(Duration.millis(500),anyoLabel);
		in.setFromX(x);
		in.setToX(0);
		
		FadeTransition in2 = new FadeTransition(Duration.millis(500),anyoLabel);
		in2.setFromValue(0);
		in2.setToValue(1);
		
		ParallelTransition pr = new ParallelTransition(out,out2,in,in2);
		pr.setOnFinished(e->getChildren().remove(oldYear));
		
		pr.play();
		onSpecialDaysChange();
	}

	private class CalendarService extends Service<Void> {
		private LocalDate hoy;
        @Override
        protected Task<Void> createTask() {
            return new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    while(!isCancelled()){
                    	 hoy =LocalDate.now();
                    	if (!today.get().equals(hoy)) {
							today.set(hoy);
						}
                    	Thread.sleep(30);
                    }
                    return null;
                }
            };
        }
    }

	

	

	
	



	public SetProperty<LocalDate> specialDaysProperty() {
		return this.specialDays;
	}
	

	public ObservableSet<LocalDate> getSpecialDays() {
	return this.specialDaysProperty().get();
	}

	public ObjectProperty<LocalDate> selectedDayProperty() {
		return this.selectedDay;
	}
	

	public LocalDate getSelectedDay() {
		return this.selectedDayProperty().get();
	}
	

	public void setSelectedDay(final LocalDate selectedDay) {
		this.selectedDayProperty().set(selectedDay);
	}
	
	

	
	
	
	



}
