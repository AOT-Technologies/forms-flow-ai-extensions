# +
import os
from fpdf import FPDF
from PIL import Image

pdf = FPDF(orientation = 'P', unit = 'mm', format='A4')

def new_pdf():
    pdf.add_page()
    
    # Set Margin
    pdf.l_margin = pdf.l_margin*1.0
    pdf.r_margin = pdf.r_margin*1.0
    pdf.t_margin = pdf.t_margin*1.0
    pdf.b_margin = pdf.b_margin*1.0
    # Effective page width and height
    epw = pdf.w - pdf.l_margin - pdf.r_margin
    eph = pdf.h - pdf.t_margin - pdf.b_margin
    # Draw new margins.
    pdf.rect(pdf.l_margin, pdf.t_margin, w=epw, h=eph)
    
    # set style and size of font 
    # that you want in the pdf
    pdf.set_font("Arial", 'BU', 18.0)
    pdf.ln(10)
    # create a cell
    pdf.cell(200, 10, txt = "New Business License Application", 
            ln = 2, align = 'C')
    pdf.ln(2)
    # add another cell
    pdf.cell(200, 10, txt = "Background Verification",
            ln = 2, align = 'C')
    pdf.ln(10)
    
def add_new_heading(headerText):
    # set style and size of font 
    pdf.set_font("Arial", 'U', size = 12)
    pdf.set_text_color(0, 0, 255)
    # add a cell
    pdf.cell(200, 0, txt = headerText,
            ln = 2, align = 'C')
               
def add_new_page(headerText):
    pdf.add_page()
    
    # Set Margin
    pdf.l_margin = pdf.l_margin*1.0
    pdf.r_margin = pdf.r_margin*1.0
    pdf.t_margin = pdf.t_margin*1.0
    pdf.b_margin = pdf.b_margin*1.0
    # Effective page width and height
    epw = pdf.w - pdf.l_margin - pdf.r_margin
    eph = pdf.h - pdf.t_margin - pdf.b_margin
    # Draw new margins.
    pdf.rect(pdf.l_margin, pdf.t_margin, w=epw, h=eph)
    pdf.ln(20)
    # set style and size of font 
    pdf.set_font("Arial", 'U', size = 12)
    pdf.set_text_color(0, 0, 255)
    # add a cell
    pdf.cell(200, 10, txt = headerText,
            ln = 2, align = 'C')
    
def add_image_file(image):
    pdf.image(image, 30, 80, 135)

def footer(self):
    # Go to 1.5 cm from bottom
    self.set_y(-15)
    # Select Arial italic 8
    self.set_font('Arial', 'I', 8)
    # Print centered page number
    self.cell(0, 10, 'Page %s' % self.page_no(), 0, 0, 'C')     

def header():    
    # Arial bold 15
    pdf.set_font('Arial','B',15);
    # Move to the right
    pdf.cell(80);
    # Title
    pdf.cell(30,10,'AOT TECHNOLOGIES',1,0,'C');
    # Line break
    pdf.ln(20);  
    
def save_pdf_file(pdfFile):
    pdf.output(pdfFile)
