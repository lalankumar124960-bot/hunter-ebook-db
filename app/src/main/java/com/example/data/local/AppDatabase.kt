package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
  entities = [
    BannerEntity::class,
    TeacherEntity::class,
    ExperimentPhotoEntity::class,
    CourseClassEntity::class,
    CoachingInfoEntity::class,
    StudentEntity::class,
    TestPaperEntity::class,
    QuestionEntity::class,
    TestSubmissionEntity::class,
    AdminUserEntity::class
  ],
  version = 2,
  exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
  abstract fun sapDao(): SapDao

  companion object {
    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
      return INSTANCE ?: synchronized(this) {
        val instance = Room.databaseBuilder(
          context.applicationContext,
          AppDatabase::class.java,
          "sap_biology_db"
        )
          .addCallback(DatabaseCallback(scope))
          .fallbackToDestructiveMigration()
          .build()
        INSTANCE = instance
        instance
      }
    }

    private class DatabaseCallback(
      private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
      override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        INSTANCE?.let { database ->
          scope.launch(Dispatchers.IO) {
            populateInitialData(database.sapDao())
          }
        }
      }

      suspend fun populateInitialData(dao: SapDao) {
        // 1. Admin
        dao.insertAdminUser(
          AdminUserEntity(
            id = 1,
            username = "admin",
            email = "admin@sapbiology.com",
            passwordHash = "admin123"
          )
        )

        // 2. Coaching Info
        dao.insertOrUpdateCoachingInfo(
          CoachingInfoEntity(
            id = 1,
            instituteName = "Sap Biology",
            tagline = "Dedicated Medical Entrance & Life Sciences Mentorship",
            locality = "Vidya Nagar, Opposite Science College Gate",
            city = "Patna",
            district = "Patna",
            state = "Bihar",
            pinCode = "800001",
            phone = "+91 98765 43210",
            email = "admissions@sapbiology.com",
            googleMapsUrl = "https://maps.google.com/?q=Patna+Science+College",
            founderName = "Dr. S. K. Anand",
            founderRole = "Founder & Chief Biology Mentor",
            founderBio = "M.Sc., Ph.D. in Botany & Cell Biology. Over 18 years dedicated to guiding pre-medical aspirants to top ranks in NEET and AIIMS with deep conceptual clarity.",
            founderPhotoSeed = "mentor_1",
            cofounderName = "Prof. Pooja Verma",
            cofounderRole = "Co-Founder & Academic Coordinator",
            cofounderBio = "M.Sc. Zoology, specialized in Human Physiology and Animal Diversity. Mentored hundreds of successful MBBS qualifiers.",
            cofounderPhotoSeed = "mentor_2",
            showCofounder = true,
            privacyPolicy = "Sap Biology is strictly committed to student privacy. Test results, contact details, and performance metrics are kept strictly private to the student and authorized coaching administrators.",
            termsAndConditions = "All study modules, questions, test series papers, and diagrams are the intellectual property of Sap Biology Institute. Tests must be answered honestly by registered students.",
            copyrightText = "© 2026 Sap Biology Institute. All rights reserved."
          )
        )

        // 3. Banners
        dao.insertBanner(
          BannerEntity(
            title = "NEET 2027 Two-Year Foundation Batch",
            subtitle = "Target 360/360 in Biology | Daily DPPs, Smart Visual Notes & Regular Mock Tests",
            tag = "ADMISSION OPEN",
            actionText = "Join Batch",
            orderIndex = 1,
            isActive = true
          )
        )
        dao.insertBanner(
          BannerEntity(
            title = "Class 12 Boards + NEET Intensive 2026",
            subtitle = "NCERT Line-by-Line Micro-Analysis & Assertion-Reason Master Classes",
            tag = "HIGH YIELD",
            actionText = "Enroll Now",
            orderIndex = 2,
            isActive = true
          )
        )
        dao.insertBanner(
          BannerEntity(
            title = "All India Sunday Online Test Series",
            subtitle = "Experience exact NTA NEET examination pattern with instant faculty reviews",
            tag = "FREE TEST",
            actionText = "Take Test",
            orderIndex = 3,
            isActive = true
          )
        )

        // 4. Teachers
        dao.insertTeacher(
          TeacherEntity(
            name = "Dr. S. K. Anand",
            subject = "Cytology, Genetics & Plant Physiology",
            experience = "18+ Years Experience",
            shortIntro = "Renowned botany author; mentored 120+ top 1000 NEET rankers.",
            bio = "Dr. Anand holds a Ph.D. in Botanical Sciences from Patna University. He is celebrated for breaking down complex biochemical cycles like photosynthesis and genetic transcription into intuitive, memorable visual stories.",
            avatarSeed = "teacher_1",
            orderIndex = 1
          )
        )
        dao.insertTeacher(
          TeacherEntity(
            name = "Prof. Ramesh Verma",
            subject = "Human Physiology & Anatomy",
            experience = "14+ Years Experience",
            shortIntro = "Expert in circulatory, nervous, endocrine and reproductive systems.",
            bio = "Prof. Verma has trained medical aspirants across Northern India. His lectures combine clinical reasoning with NCERT-strict guidelines, ensuring students never lose marks on tricky physiological diagrams.",
            avatarSeed = "teacher_2",
            orderIndex = 2
          )
        )
        dao.insertTeacher(
          TeacherEntity(
            name = "Dr. Ananya Sen",
            subject = "Ecology, Evolution & Biotechnology",
            experience = "11+ Years Experience",
            shortIntro = "Gold medalist in Life Sciences; specialist in recombinant DNA technology.",
            bio = "Dr. Sen is an enthusiastic educator whose interactive slide decks and experimental demonstrations make molecular biology come alive. She conducts dedicated doubt sessions every weekend.",
            avatarSeed = "teacher_3",
            orderIndex = 3
          )
        )
        dao.insertTeacher(
          TeacherEntity(
            name = "Prof. K. N. Gupta",
            subject = "Animal Kingdom & Biomolecules",
            experience = "16+ Years Experience",
            shortIntro = "Master of taxonomy, comparative anatomy and metabolic biochemistry.",
            bio = "Prof. Gupta has spent 16 years teaching zoological taxonomy and amino acid metabolism. His mnemonics for animal phyla characteristics are legendary among pre-medical students.",
            avatarSeed = "teacher_4",
            orderIndex = 4
          )
        )

        // 5. Experiment Moments
        dao.insertExperimentPhoto(
          ExperimentPhotoEntity(
            title = "Onion Root Tip Mitosis Squash",
            caption = "Class 11 students observing metaphase and anaphase chromosomes under high-power compound microscope.",
            tag = "Cytology Lab",
            photoSeed = "mitosis",
            orderIndex = 1
          )
        )
        dao.insertExperimentPhoto(
          ExperimentPhotoEntity(
            title = "Paper Chromatography of Chlorophyll",
            caption = "Separating photosynthetic pigments (Chlorophyll-a, b, Xanthophyll, Carotene) using petroleum ether solvent.",
            tag = "Plant Physiology",
            photoSeed = "chromatography",
            orderIndex = 2
          )
        )
        dao.insertExperimentPhoto(
          ExperimentPhotoEntity(
            title = "DNA Isolation from Fruit Tissue",
            caption = "Precipitating white genomic DNA spooling threads using ice-cold ethanol after cell lysis.",
            tag = "Biotechnology",
            photoSeed = "dna_spooling",
            orderIndex = 3
          )
        )
        dao.insertExperimentPhoto(
          ExperimentPhotoEntity(
            title = "Plasmolysis in Tradescantia Leaf",
            caption = "Studying osmotic cell shrinkage and hypertonic response under 400x magnification.",
            tag = "Osmosis Lab",
            photoSeed = "plasmolysis",
            orderIndex = 4
          )
        )
        dao.insertExperimentPhoto(
          ExperimentPhotoEntity(
            title = "Human Blood Smear & Differential WBC",
            caption = "Identifying neutrophils, lymphocytes and platelets stained with Leishman's stain.",
            tag = "Hematology",
            photoSeed = "blood_cells",
            orderIndex = 5
          )
        )
        dao.insertExperimentPhoto(
          ExperimentPhotoEntity(
            title = "Stomatal Distribution & Peel Mount",
            caption = "Comparing stomatal density between dorsal and ventral epidermis of dicot vs monocot leaves.",
            tag = "Plant Anatomy",
            photoSeed = "stomata",
            orderIndex = 6
          )
        )

        // 6. Classes We Teach
        dao.insertCourse(
          CourseClassEntity(
            title = "Class 11 - NEET Elite Medical Batch",
            grade = "Class 11",
            targetAudience = "CBSE / ICSE / State Board + NEET Aspirants",
            description = "Building solid biological intuition covering Cell Biology, Plant & Human Physiology, and Structural Organization.",
            featuresList = "Daily 2-hour conceptual lectures;Line-by-line NCERT decoding;Weekly objective tests;Hands-on microscope sessions",
            timings = "Monday to Saturday: 4:30 PM - 6:30 PM",
            orderIndex = 1
          )
        )
        dao.insertCourse(
          CourseClassEntity(
            title = "Class 12 - Board & NEET Intensive",
            grade = "Class 12",
            targetAudience = "Class 12 Medical Aspirants aiming for 95%+ & 350+ in NEET Bio",
            description = "Mastering Genetics, Molecular Biology, Biotechnology, Human Reproduction, and Ecology with extensive question banks.",
            featuresList = "Syllabus completed well before Board exams;Assertion-Reasoning specialization;15-year NEET question drills;Doubt counters",
            timings = "Monday to Saturday: 6:45 PM - 8:45 PM",
            orderIndex = 2
          )
        )
        dao.insertCourse(
          CourseClassEntity(
            title = "Target NEET - Repeater / Dropper Batch",
            grade = "12th Passed",
            targetAudience = "Dedicated 1-Year Fast Track for 650+ NEET Score",
            description = "Comprehensive revision of Class 11 & 12 Biology with continuous speed-accuracy tests and personal faculty mentors.",
            featuresList = "Full syllabus coverage in 7 months;Daily Speed Drills (45 questions / 35 min);Detailed individual test analytics;All India test ranking",
            timings = "Monday to Friday: 9:00 AM - 1:00 PM",
            orderIndex = 3
          )
        )
        dao.insertCourse(
          CourseClassEntity(
            title = "Foundation Batch - Class 9 & 10",
            grade = "Class 9 & 10",
            targetAudience = "Junior Science Olympiad, NTSE & Early Pre-Medical",
            description = "Inculcating scientific curiosity, life process fundamentals, and experimental logic from an early stage.",
            featuresList = "Olympiad-standard problem solving;Microscopy and plant specimen labs;Interactive biology model making;Concept building",
            timings = "Tuesday, Thursday, Saturday: 3:00 PM - 4:30 PM",
            orderIndex = 4
          )
        )

        // 7. Test Papers & Questions
        // Paper 1: Cell Structure & Function (NEET Mock)
        val paper1Id = dao.insertPaper(
          TestPaperEntity(
            title = "NEET Biology Mock: Cell Structure & Biomolecules",
            subject = "Biology",
            instructions = "This test contains 10 objective MCQs. Each question carries 1 mark. There is no negative marking. Select one option among A, B, C, D.",
            durationMinutes = 15,
            totalQuestions = 10,
            isPublished = true
          )
        )

        dao.insertQuestion(
          QuestionEntity(
            paperId = paper1Id,
            questionNumber = 1,
            questionText = "Which of the following cell organelles is known as the 'suicide bag' of the cell?",
            optionA = "Ribosome",
            optionB = "Lysosome",
            optionC = "Peroxisome",
            optionD = "Golgi Apparatus",
            correctAnswer = "B",
            explanation = "Lysosomes contain hydrolytic enzymes active at acidic pH capable of digesting cellular components."
          )
        )
        dao.insertQuestion(
          QuestionEntity(
            paperId = paper1Id,
            questionNumber = 2,
            questionText = "The 70S ribosomes are found in which of the following?",
            optionA = "Prokaryotes and Chloroplasts",
            optionB = "Eukaryotic cytoplasm only",
            optionC = "Only in animal cell nuclei",
            optionD = "Golgi bodies",
            correctAnswer = "A",
            explanation = "70S ribosomes occur in prokaryotic cytoplasm and semi-autonomous eukaryotic organelles like chloroplasts and mitochondria."
          )
        )
        dao.insertQuestion(
          QuestionEntity(
            paperId = paper1Id,
            questionNumber = 3,
            questionText = "According to the Fluid Mosaic Model of Singer and Nicolson, the cell membrane is composed of:",
            optionA = "A continuous protein bilayer with embedded lipids",
            optionB = "Phospholipid bilayer with extrinsic and intrinsic proteins",
            optionC = "Cellulose and pectin lattice",
            optionD = "Chitin sheets interspersed with sterols",
            correctAnswer = "B",
            explanation = "The Fluid Mosaic Model describes proteins floating like icebergs in a quasi-fluid sea of phospholipids."
          )
        )
        dao.insertQuestion(
          QuestionEntity(
            paperId = paper1Id,
            questionNumber = 4,
            questionText = "During which stage of mitosis do sister chromatids separate and move towards opposite poles?",
            optionA = "Prophase",
            optionB = "Metaphase",
            optionC = "Anaphase",
            optionD = "Telophase",
            correctAnswer = "C",
            explanation = "In anaphase, centromeres split and sister chromatids are pulled towards opposite spindle poles."
          )
        )
        dao.insertQuestion(
          QuestionEntity(
            paperId = paper1Id,
            questionNumber = 5,
            questionText = "Which biomolecule is the most abundant chemical compound in living organisms?",
            optionA = "Proteins",
            optionB = "Lipids",
            optionC = "Water",
            optionD = "Carbohydrates",
            correctAnswer = "C",
            explanation = "Water constitutes 70% to 90% of total cellular mass."
          )
        )
        dao.insertQuestion(
          QuestionEntity(
            paperId = paper1Id,
            questionNumber = 6,
            questionText = "Crossing over during meiosis occurs during which sub-stage of Prophase I?",
            optionA = "Leptotene",
            optionB = "Zygotene",
            optionC = "Pachytene",
            optionD = "Diakinesis",
            correctAnswer = "C",
            explanation = "Recombination nodules and crossing over between non-sister chromatids take place during pachytene mediated by recombinase."
          )
        )
        dao.insertQuestion(
          QuestionEntity(
            paperId = paper1Id,
            questionNumber = 7,
            questionText = "Which bond links the adjacent nucleotides in a polynucleotide DNA chain?",
            optionA = "Glycosidic bond",
            optionB = "Phosphodiester bond",
            optionC = "Peptide bond",
            optionD = "Hydrogen bond",
            correctAnswer = "B",
            explanation = "A 3'-5' phosphodiester bond links the 3'-OH of one deoxyribose to the 5'-phosphate of the next."
          )
        )
        dao.insertQuestion(
          QuestionEntity(
            paperId = paper1Id,
            questionNumber = 8,
            questionText = "Which organelle is responsible for the synthesis of steroidal hormones in animal cells?",
            optionA = "Smooth Endoplasmic Reticulum (SER)",
            optionB = "Rough Endoplasmic Reticulum (RER)",
            optionC = "Mitochondria",
            optionD = "Centriole",
            correctAnswer = "A",
            explanation = "Smooth ER is the major site for lipid synthesis, including steroidal hormones in animal cells."
          )
        )
        dao.insertQuestion(
          QuestionEntity(
            paperId = paper1Id,
            questionNumber = 9,
            questionText = "The non-protein chemical component required for enzyme biological activity is termed:",
            optionA = "Apoenzyme",
            optionB = "Cofactor",
            optionC = "Holoenzyme",
            optionD = "Isozyme",
            correctAnswer = "B",
            explanation = "Cofactors are non-protein compounds bound to enzymes to assist in chemical transformations."
          )
        )
        dao.insertQuestion(
          QuestionEntity(
            paperId = paper1Id,
            questionNumber = 10,
            questionText = "The internal folding of the inner mitochondrial membrane is called:",
            optionA = "Cristae",
            optionB = "Cisternae",
            optionC = "Thylakoids",
            optionD = "Granum",
            correctAnswer = "A",
            explanation = "Cristae increase the surface area of the inner mitochondrial membrane for ATP synthase and electron transport chain complexes."
          )
        )

        // Paper 2: Genetics & Principles of Inheritance
        val paper2Id = dao.insertPaper(
          TestPaperEntity(
            title = "Class 12: Genetics & Mendelian Inheritance",
            subject = "Biology",
            instructions = "10 questions on Mendelian laws, linkage, and sex determination. Each question is 1 mark. Total 10 marks. Duration 15 minutes.",
            durationMinutes = 15,
            totalQuestions = 8,
            isPublished = true
          )
        )

        dao.insertQuestion(
          QuestionEntity(
            paperId = paper2Id,
            questionNumber = 1,
            questionText = "What is the expected phenotypic ratio in the F2 generation of a classic Mendelian monohybrid cross with complete dominance?",
            optionA = "1:2:1",
            optionB = "3:1",
            optionC = "9:3:3:1",
            optionD = "2:1",
            correctAnswer = "B",
            explanation = "The phenotypic ratio is 3 dominant to 1 recessive."
          )
        )
        dao.insertQuestion(
          QuestionEntity(
            paperId = paper2Id,
            questionNumber = 2,
            questionText = "A cross between an F1 individual and its homozygous recessive parent is called a:",
            optionA = "Reciprocal cross",
            optionB = "Test cross",
            optionC = "Back cross to dominant",
            optionD = "Monohybrid selfing",
            correctAnswer = "B",
            explanation = "A test cross identifies whether an individual showing dominant phenotype is homozygous or heterozygous."
          )
        )
        dao.insertQuestion(
          QuestionEntity(
            paperId = paper2Id,
            questionNumber = 3,
            questionText = "In human blood groups, ABO inheritance demonstrates both multiple allelism and:",
            optionA = "Incomplete dominance",
            optionB = "Codominance",
            optionC = "Epistasis",
            optionD = "Pleiotropy",
            correctAnswer = "B",
            explanation = "Alleles IA and IB are codominant with each other, expressing AB blood group."
          )
        )
        dao.insertQuestion(
          QuestionEntity(
            paperId = paper2Id,
            questionNumber = 4,
            questionText = "The physical association of two genes located on the same chromosome was termed by T.H. Morgan as:",
            optionA = "Crossing over",
            optionB = "Linkage",
            optionC = "Recombination",
            optionD = "Translocation",
            correctAnswer = "B",
            explanation = "Morgan coined 'linkage' to describe genes staying together during inheritance on Drosophila chromosomes."
          )
        )
        dao.insertQuestion(
          QuestionEntity(
            paperId = paper2Id,
            questionNumber = 5,
            questionText = "Down's syndrome in humans is caused by which genetic abnormality?",
            optionA = "Monosomy of X chromosome",
            optionB = "Trisomy of chromosome 21",
            optionC = "Trisomy of chromosome 18",
            optionD = "XXY sex chromosome constitution",
            correctAnswer = "B",
            explanation = "Down's syndrome is due to trisomy of autosome 21 caused by non-disjunction."
          )
        )
        dao.insertQuestion(
          QuestionEntity(
            paperId = paper2Id,
            questionNumber = 6,
            questionText = "Which of the following is an example of an X-linked recessive genetic disorder?",
            optionA = "Phenylketonuria",
            optionB = "Sickle cell anemia",
            optionC = "Hemophilia",
            optionD = "Cystic fibrosis",
            correctAnswer = "C",
            explanation = "Hemophilia is sex-linked recessive, carried on the X chromosome."
          )
        )
        dao.insertQuestion(
          QuestionEntity(
            paperId = paper2Id,
            questionNumber = 7,
            questionText = "In Drosophila melanogaster, sex determination is based on:",
            optionA = "Ratio of X chromosomes to sets of autosomes (X/A ratio)",
            optionB = "Presence of Y chromosome solely",
            optionC = "Temperature during egg incubation",
            optionD = "Haplo-diploid mechanism",
            correctAnswer = "A",
            explanation = "C.B. Bridges proved genic balance theory: X/A ratio determines sex in Drosophila."
          )
        )
        dao.insertQuestion(
          QuestionEntity(
            paperId = paper2Id,
            questionNumber = 8,
            questionText = "Sickle-cell anemia is caused by a point mutation leading to substitution of:",
            optionA = "Glutamic acid by Valine at 6th position of beta-globin",
            optionB = "Valine by Glutamic acid at 6th position of alpha-globin",
            optionC = "Glycine by Valine at 5th position",
            optionD = "Lysine by Alanine at 6th position",
            correctAnswer = "A",
            explanation = "GAG codon mutate to GUG replacing Glutamic acid with Valine at codon 6 of beta-globin chain."
          )
        )

        // Demo sample student
        dao.insertStudent(
          StudentEntity(
            fullName = "Aarav Sharma",
            mobileNumber = "9876543210",
            passwordHash = "student123",
            registeredAt = System.currentTimeMillis() - 86400000L
          )
        )
      }
    }
  }
}
